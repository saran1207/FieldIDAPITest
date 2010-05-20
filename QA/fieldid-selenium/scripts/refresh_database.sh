#!/bin/sh

mysql -uroot -e "drop database fieldid_minimal_security; create database fieldid_minimal_security; use fieldid_minimal_security;  \. ../resources/minimal-security-backup.sql "

