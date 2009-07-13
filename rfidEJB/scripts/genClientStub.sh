#!/bin/bash

export JAVA_HOME="/opt/java/jdk1.5.0_15"
ANTHOME="/opt/java/ant"

xFireHome="/home/mfrederi/software/xfire"

OUTPUTPACKAGE="com.n4systems.webservice.client"

xFireJar="${xFireHome}/xfire-all-1.2.6.jar"
xFireLib="${xFireHome}/lib"

ANTJAR="${ANTHOME}/lib/ant.jar"

OUTPUTDIRBASE=`pwd`

WSGENCLASS="org.codehaus.xfire.gen.WsGen"

JARS="${xFireJar}
${ANTJAR}
${xFireLib}/jaxb-api-2.0.jar
${xFireLib}/stax-api-1.0.1.jar
${xFireLib}/jdom-1.0.jar
${xFireLib}/jaxb-impl-2.0.1.jar
${xFireLib}/jaxb-xjc-2.0.1.jar
${xFireLib}/wstx-asl-3.2.0.jar
${xFireLib}/commons-logging-1.0.4.jar
${xFireLib}/activation-1.1.jar
${xFireLib}/wsdl4j-1.6.1.jar
${xFireLib}/XmlSchema-1.1.jar
${xFireLib}/xfire-jsr181-api-1.0-M1.jar"

function usage() {
	echo "$0 <WSDL Url> <jar name>"
	exit 0
}

BUILDDIR="build"
SRCDIR="src"

function createBuildXml() {
cat > build.xml <<BUILDFILE
<project name="FieldId Webservice Client" default="build" basedir=".">
<target name="build" >
	<mkdir dir="${BUILDDIR}" />
	<javac srcdir="${SRCDIR}" destdir="${BUILDDIR}">
		<classpath>
			<pathelement path="${xFireJar}" />
			<fileset dir="${xFireLib}">
				<include name="**/*.jar" />
			</fileset>
		</classpath>
	</javac>
	<jar jarfile="${OUTPUTDIRBASE}/${jarName}.jar" basedir="${BUILDDIR}">
		<metainf dir="${SRCDIR}/META-INF" includes="**"/>
	</jar>
</target>
</project>
BUILDFILE
}

if [ "$#" -lt "2" ]; then
	usage
fi

wsdlUrl="$1"
jarName="$2"

jarRoot="${OUTPUTDIRBASE}/${jarName}"
outputDir="${jarRoot}/${SRCDIR}"

export CLASSPATH=""
JARSEP=":"

for jar in $JARS; do
	if [ ! -f "$jar" ]; then
		echo "Unable to find dependant lib: ${jar}"
		exit 1
	fi
	CLASSPATH="${CLASSPATH}${jar}${JARSEP}"
done

if [ ! -d "${outputDir}" ]; then
	mkdir -p $outputDir
fi

${JAVA_HOME}/bin/java $WSGENCLASS -wsdl "${wsdlUrl}" -externalBindings "bindings.xml" -binding "jaxb" -o "${outputDir}" -p "${OUTPUTPACKAGE}" -overwrite "true"

cd "${jarRoot}"

createBuildXml
${ANTHOME}/bin/ant

rm -rf ${jarRoot}

exit 0
