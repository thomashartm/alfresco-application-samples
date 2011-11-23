#!/bin/bash

RESOURCES=${SCRIPT_DIR}/resources

# prepare correct JAVA_OPTS
J_OPTS="-Xms512m -Xmx1024m -Xss1024k -XX:MaxPermSize=256m -XX:NewSize=256m -server -Dcom.sun.management.jmxremote -Duser.language=en -Duser.country=en  -Duser.region=US"

# copy windows batch scripts if we are in a windows environment ... skip on any *nix
if [[ $ENV_INFO == *_NT* ]]; then
	echo -n "Windows environment detected. "
  echo -n "Preparing *.bat start scripts ..."
  
  cp ${RESOURCES}/*.bat ${DEST_DIR}/
  sed -i ${DEST_DIR}/alfresco.bat -e "s/rem JAVA_OPTS.*/set JAVA_OPTS=\"${J_OPTS} -Dalfresco.home=%ALF_HOME%\"/g"
  sed -i ${DEST_DIR}/alfresco.bat -e 's/$'"/`echo \\\r`/" 
   
  echo_outcome $?
fi

# copy start shell scripts ... in all environments
echo -n "Preparing *.sh start scripts ..."
cp -R ${RESOURCES}/*.sh ${DEST_DIR}/

# set path to catalina.sh
sed -i ${DEST_DIR}/start.sh -e "/^# resolve links/a PRGDIR=${DEST_DIR}/tomcat/bin"

# set correct java opts and home path
sed -i ${DEST_DIR}/tomcat/bin/catalina.sh -e "/^# OS specific/i #ALF_JAVA_OPTS"
sed -i ${DEST_DIR}/tomcat/bin/catalina.sh -e "/^#ALF_JAVA_OPTS/a export JAVA_OPTS=\"${J_OPTS} -Dalfresco.home=${DEST_DIR}\""

echo_outcome $?