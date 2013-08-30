package com.fieldid.jdbc;


import com.mysql.jdbc.JDBC4Connection;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.JdbcInterceptor;
import org.apache.tomcat.jdbc.pool.PooledConnection;
import org.apache.tomcat.jdbc.pool.ProxyConnection;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionInterceptor extends JdbcInterceptor {
	private static final Log logger = LogFactory.getLog(ConnectionInterceptor.class);

	private static final Map<Long, ActiveConnection> connectionsInUse = new ConcurrentHashMap<Long, ActiveConnection>();

	public static Map<Long, ActiveConnection> getConnectionsInUse() {
		return connectionsInUse;
	}

	private JDBC4Connection getConnectionFromProxy(Object proxy) {
		JDBC4Connection con = null;
		if (proxy instanceof ProxyConnection) {
			con = (JDBC4Connection) ((ProxyConnection) proxy).getConnection().getConnection();
		} else if (proxy instanceof JDBC4Connection) {
			con = (JDBC4Connection) proxy;
		} else if (proxy instanceof Connection) {
			try {
				con = ((Connection) proxy).unwrap(JDBC4Connection.class);
			} catch (SQLException e) {
				logger.error("Cannot unrwap " + proxy.getClass().getName() + " to " + JDBC4Connection.class.getName(), e);
			}
		} else {
			logger.warn("Unknown Proxy: " + proxy.getClass().getName());
		}
		return con;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("close")) {
			JDBC4Connection con = getConnectionFromProxy(proxy);
			if (con != null) {
				if (connectionsInUse.remove(con.getId()) == null) {
					logger.warn("close called on non-active connection [" + con.getId() + "]");
				}
			}
		} else if (method.getName().equals("prepareStatement")) {
			JDBC4Connection con = getConnectionFromProxy(proxy);
			if (con != null) {
				ActiveConnection ac = connectionsInUse.get(con.getId());
				if (ac != null) {
					if (args != null && args.length >= 1) {
						ac.getStatements().add(new ExecutedStatement(java.lang.System.currentTimeMillis() - ac.getStarted(), args[0].toString(), Thread.currentThread().getStackTrace()));
					}
				}
			}
		}

		return super.invoke(proxy, method, args);
	}

	@Override
	public void reset(ConnectionPool parent, PooledConnection con) {
		if (con == null || con.getConnection() == null) return;

		ActiveConnection activeConn = new ActiveConnection(((JDBC4Connection) con.getConnection()).getId(), Thread.currentThread().getName(), System.currentTimeMillis());
		if (connectionsInUse.put(activeConn.getId(), activeConn) != null) {
			logger.warn("reset called on borrowed connection [" + activeConn.getId() + "]");
		}
	}

	@Override
	public void disconnected(ConnectionPool parent, PooledConnection con, boolean finalizing) {
		logger.info(String.format("disconnected(%s, %s, %b)", parent, con, finalizing));
	}

	@Override
	public void poolClosed(ConnectionPool pool) {
		logger.info(String.format("poolClosed(%s)", pool));
	}

	@Override
	public void poolStarted(ConnectionPool pool) {
		logger.info(String.format("poolStarted(%s)", pool));
	}

}
