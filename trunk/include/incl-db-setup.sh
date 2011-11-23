#!/bin/bash

echo "chosen database name : $ALF_DB_NAME"

# copy setup and remove scripts to extras location 
echo -n "preparing the db_setup.sql and db_remove.sql scripts ... "

if [[ ! -f  ${DEST_DIR}/extras ]];then
 mkdir ${DEST_DIR}/extras
fi
cp ${SCRIPT_DIR}/sql/mysql/*.sql ${DEST_DIR}/extras
  
# replace only the first 'alfresco' occurence on the line : the db, not the user/password
sed -i ${DEST_DIR}/extras/db_setup.sql -e "s/alfresco/$ALF_DB_NAME/"
sed -i ${DEST_DIR}/extras/db_setup.sql -e "s/create database/drop database if exists $ALF_DB_NAME;\n&/"


sed -i ${DEST_DIR}/extras/db_remove.sql -e "s/alfresco/$ALF_DB_NAME/"
# comment the bottom 2 lines (drop user alfresco SQL). user is shared between all DBs
sed -i ${DEST_DIR}/extras/db_remove.sql -e '2,3 s/^/-- /'

echo_outcome $?


# run the sql creation scripts
echo -n "setting up DB $ALF_DB_NAME" 
#echo -n "for ENV "
#echo  -n ${ENV_INFO}
echo_outcome $?

# Using a groovy script to run sql as win/cygwin envrinoments have no access to the mysql commandline client 
# Other db are possible with this approach
# The process relies on our generated, installation specific sql files
echo -n "Execute dbsetup.groovy: "

#TODO: ADD password
groovy groovy/dbsetup.groovy -h -s ${DEST_DIR}/extras/db_remove.sql -u ${ALF_DB_USER}
groovy groovy/dbsetup.groovy -h -s ${DEST_DIR}/extras/db_setup.sql -u ${ALF_DB_USER}
echo_outcome $?

# add additional mysql connector (postgres driver is bundled starting with 3.5)
echo -n "copying MySQL driver in tomcat/lib"
cp -p ${MYSQL_DRIVER_LOCATION} ${TOMCAT_LIB_DIR}
echo_outcome $?