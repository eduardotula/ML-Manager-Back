#!/bin/sh

echo DB_HEROKU_SPLIT=[$DB_HEROKU_SPLIT]
echo ${DATABASE_URL#*@}

if [[ "${DB_HEROKU_SPLIT,,}" == "true" ]]; then

  export DB_JDBC_URL=jdbc:postgresql://${DATABASE_URL#*@}

  export DB_JDBC_USER=$(expr $DATABASE_URL : '.*/\([^:]*\):.*')

  export DB_JDBC_PASSWORD=$(expr $DATABASE_URL : '.*:\([^@]*\)@.*')
fi