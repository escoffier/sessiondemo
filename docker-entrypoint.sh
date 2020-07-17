#!/bin/sh

if [ -z "$SPRING_DATASOURCE_URL" ]; then
  SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/employees?useSSL=true"
fi

if [ -z "$DB_PASSWORD" ]; then
  DB_PASSWORD="19811981"
fi

#url="jdbc:mysql://${SPRING_DB_HOST}/employees?useSSL=true"

echo "################################################"
export
echo "################################################"
echo "########DB_URLT = $SPRING_DATASOURCE_URL"
echo "########SPRING_DATASOURCE_PASSWORD = $SPRING_DATASOURCE_PASSWORD"
echo "################################################"

#java -cp app:app/lib/* -Dspring.datasource.password="${DB_PASSWORD}" "com.example.sessiondemo.SessiondemoApplication"
java -cp app:app/lib/*  "com.example.sessiondemo.SessiondemoApplication"

exec "$@"