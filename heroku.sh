#!/bin/sh
export DB_JDBC_URL=jdbc:postgresql://${DATABASE_URL#*@}
export DB_JDBC_USER=$(expr $DATABASE_URL : '.*/\([^:]*\):.*')
export DB_JDBC_PASSWORD=$(expr $DATABASE_URL : '.*:\([^@]*\)@.*')
