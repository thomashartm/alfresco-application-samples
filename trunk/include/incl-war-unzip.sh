#!/bin/bash

#extracting tomcat barebones
echo -n "extracting tomcat barebones into ${DEST_DIR} ..."
tar zxf ${TOMCAT_BASE_ARCHIVE_LOCATION} -C ${DEST_DIR}
echo_outcome $? 

# rename the apache-tomcat-* folder to tomcat    
if [ ! -d ${TOMCAT_ROOT} ]; then  
    echo -n "renaming tomcat base directory to ${TOMCAT_ROOT} ..."   
    mv ${DEST_DIR}/*-tomcat-* ${TOMCAT_ROOT}
    echo_outcome $?
fi

ARCHIVE_EXTENSION=${INPUT_ARCHIVE_NAME##*.}
if [ "$ARCHIVE_EXTENSION" == "zip" ];then
    
    TEMP_DIR=/tmp/${ALF_VERSION}-$(date '+%s')
    mkdir -p ${TEMP_DIR}
    
    echo -n "extracting zip bundle into dest dir  ${DEST_DIR} ..."
	  unzip  -q -B -o ${INPUT_ARCHIVE_PATH} -d ${TEMP_DIR}
	  echo_outcome $?
      
    # extracting tomcat specific stuff  and webapps
    echo -n "extracting web-server directory into ${TOMCAT_ROOT} ..."
	  cp -R ${TEMP_DIR}/web-server/* ${TOMCAT_ROOT}
	  chmod -R 777 ${TOMCAT_ROOT}
	  echo_outcome $?
	  
	  # extracting licence information
	  echo -n "extracting licenses directory into ${DEST_DIR} ..."
    cp -R ${TEMP_DIR}/licenses ${DEST_DIR}/licenses
    echo_outcome $?
    
    # extracting bin directory
    echo -n "extracting bin directory into ${BIN_DIR} ..."
    if [ ! -d  ${BIN_DIR} ]; then
       mkdir -p ${BIN_DIR}
    fi
	  cp -R ${TEMP_DIR}/bin/*.sh ${BIN_DIR}
	  cp -R ${TEMP_DIR}/bin/*.jar ${BIN_DIR}
	  echo_outcome $?
	  
	  chmod -R 755 ${BIN_DIR}
	  
	  # checking if windows os and unpacking windows specific files if true
	  
    if [[ $ENV_INFO == *_NT* ]]; then
	   echo -n "Windows environment detected. Unpacking environment specific files ..."
     cp -R ${TEMP_DIR}/bin/*.bat ${BIN_DIR}
	   cp -R ${TEMP_DIR}/bin/*.dll ${TOMCAT_ROOT}/bin
	   echo_outcome $?
	  fi
	  
      
    # TODO create startup skript
    #mv ${DEST_DIR}/alfresco_mine.sh ${DEST_DIR}/alfresco.sh
    
    if [ -f ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties.sample ]; then
	     mv ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties.sample ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties
    fi

    mv ${TOMCAT_SHARED_DIR}/classes/alfresco/web-extension/share-config-custom.xml.sample ${TOMCAT_SHARED_DIR}/classes/alfresco/web-extension/share-config-custom.xml

    rm -r ${TEMP_DIR}
    
    # set shared configuration location
    sed -i ${TOMCAT_ROOT}/conf/catalina.properties -e 's/shared.loader=.*/shared.loader=\$\{catalina.base\}\/shared\/classes,\$\{catalina.base\}\/shared\/lib\/*.jar/g'
      
else
    echo -n "unhandled extension. handles only zip or war bundles"
    echo_failure
    exit 2
fi

echo -n "new version install unpacked in ${DEST_DIR}"
echo_success