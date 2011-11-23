#!/bin/bash

#extracting tomcat barebones
echo "starting alfresco-global.properties configuration"

# Contentstore location
echo -n "store location configuration ..."
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^#dir.root/a dir.root=$DIR_ROOT_ESCAPED"
echo_outcome $? 

# Database configuration
echo -n "database configuration ..."
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e 's/#db.username=alfresco/db.username=alfresco/g'
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e 's/#db.password=alfresco/db.password=alfresco/g'
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^db\.password/a db.name=$ALF_DB_NAME"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^db\.name/a db.host=localhost"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^db\.host/a db.port=3306"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e 's/#db.url=jdbc:mysql:\/\/localhost\/alfresco/db.url=jdbc:mysql:\/\/${db.host}:${db.port}\/${db.name}/g'
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e 's/#db.driver=org.gjt.mm.mysql.Driver/db.driver=org.gjt.mm.mysql.Driver/g'
echo_outcome $? 

# 3rd party
echo -n "3rd party lib configuration ..."
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#ooo.exe=.*/ooo.exe=$LOC_SOFFICE/g"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#ooo.enabled=.*/ooo.enabled=$SOFFICE_ENABLED/g"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#img.root=.*/img.root=$LOC_IMG/g"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#swf.exe=.*/swf.exe=$LOC_SWF/g"
echo_outcome $? 

# imap configuration
echo -n "imap configuration ..."
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#imap.server.enabled=.*/imap.server.enabled=$IMAP_ENABLED/g"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#imap.server.port=.*/imap.server.port=143/g"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "s/#imap.server.host=.*/imap.server.host=localhost/g"
echo_outcome $?

# file servers : cifs
echo -n "filesystems configuration ..."
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^imap\.server\.host/a #cifs"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^#cifs/a ftp.enabled=$FTP_ENABLED"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^#cifs/a nfs.enabled=$NFS_ENABLED"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^#cifs/a cifs.enabled=$CIFS_ENABLED"
echo_outcome $?

# additional configs
echo -n "deactivate email siteinvites ..."
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "$ a #"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "$ a #additional configurations"
sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "$ a notification.email.siteinvite=false"
echo_outcome $?




    # file servers : ftp
#sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^cifs\.netBIOSSMB\.sessionPort/a #ftp"
#sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^#ftp/a ftp.port=2121"

# top up by a blank comment line
#sed -i ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties -e "/^#imap\.server\.host/a #"

# remove .sample extensions from share/explorer config
#if [ -f ${TOMCAT_SHARED_DIR}/classes/alfresco/extension/web-client-config-custom.xml.sample ]; then
#    mv ${TOMCAT_SHARED_DIR}/classes/alfresco/extension/web-client-config-custom.xml.sample ${TOMCAT_SHARED_DIR}/classes/alfresco/extension/web-client-config-custom.xml
#fi

# TODO ... Add ldap configuration