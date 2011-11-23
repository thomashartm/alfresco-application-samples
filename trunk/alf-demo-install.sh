#!/bin/bash

#************************************************#
#            alf_demo_install.sh                 #
#           written by Thomas Hartmann           #
#      based on a Romain Guinots install scripts #
#                October 04, 2011                #
#                                                #
#  Install a complete alfresco demo environment  #
#  Please make sure that you always use LF       #
#************************************************#

# Associative array that contains the alfresco versions


# Note that quoting permits embedding whitespace.

DEFAULT_ALFRESCO_RELEASE=/cygdrive/c/software/alfresco/enterprise4.0/4.0.0b/alfresco-enterprise-4.0.0b.zip
TOMCAT_BASE_ARCHIVE_LOCATION=/home/thomas/software/tomcat/apache-tomcat-6.0.33.tar.gz
MYSQL_DRIVER_LOCATION=/home/thomas/software/mysql/mysql-connector-java-5.1.7-bin.jar
SPP_AMP_LOCATION="/cygdrive/c/software/alfresco/enterprise4.0/4.0.0b/alfresco-enterprise-spp-4.0.0b.zip"

# ------------------------------------------------
# Script includes
# ------------------------------------------------
SCRIPT_DIR=$(dirname $(readlink -f $0))
source ${SCRIPT_DIR}/include/incl-util-functions.sh

ENV_INFO=`uname -a`

# ------------------------------------------------
# Executable code starts here
# ------------------------------------------------
# start and set input archive path
if [ $# -lt 1 ]; then
    echo_usage
fi

if [ "default" == $1 ]; then
  INPUT_ARCHIVE_PATH=${DEFAULT_ALFRESCO_RELEASE}
else
  INPUT_ARCHIVE_PATH=$1
fi
INPUT_ARCHIVE_NAME=$(basename $INPUT_ARCHIVE_PATH)

if [ ! -f ${INPUT_ARCHIVE_PATH} ]; then
    echo -ne "input file\t: '${INPUT_ARCHIVE_PATH}' does not exist !"
    echo_failure
    echo_usage
fi

# find out version
#ALF_VERSION=$(echo $INPUT_ARCHIVE_NAME | sed -e 's/.*-\(.*\)\..*/\1/g') # between last - and last .
ALF_VERSION="demo"
ALF_VERSION_DB_FRIENDLY=$(echo $ALF_VERSION | sed -e 's/\.//g' | tr '-' '_')

DEST_DIR="c:\/alfresco\/$ALF_VERSION"
BIN_DIR=${DEST_DIR}/bin 
#DIR_ROOT="/opt/alfresco/data/$ALF_VERSION"
DIR_ROOT="c:\/alfresco\/data\/$ALF_VERSION"
SUBSYS_DIR="classes/alfresco/extension/subsystems"

# init database name
ALF_VERSION_DB_FRIENDLY=$(echo $ALF_VERSION | sed -e 's/\.//g' | tr '-' '_')
ALF_DB_NAME="alfresco$ALF_VERSION_DB_FRIENDLY"
ALF_DB_USER="root"
ALF_DB_PASSWORD=""
DIR_ROOT_ESCAPED=$(echo $ALF_VERSION | sed -e 's/\.//g' | tr '-' '_')

# 3rd party lib locations - please use fully escaped paths 
LOC_IMG="c:\/alfresco\/bin\/ImageMagick"
LOC_SWF="c:\/alfresco\/bin\/swftools\/pdf2swf"
LOC_SOFFICE="c:\/Program Files (x86)\/OpenOffice.org 3\/program\/soffice"
SOFFICE_ENABLED="false"

#Filesystems
IMAP_ENABLED="true"
CIFS_ENABLED="true"
FTP_ENABLED="false"
NFS_ENABLED="false"

# Tomcat root path

# cleanup the exiting alfresco and repository directories - development mode
# requires the clean argument as second argument
if [[ $# -eq 2 ]]; then
  install_cleanup $2
fi

make_dir ${DEST_DIR}
make_dir ${DIR_ROOT}
   
# Tomcat root path
TOMCAT_ROOT=${DEST_DIR}/tomcat
TOMCAT_SHARED_DIR=${TOMCAT_ROOT}/shared
TOMCAT_WEBAPPS_DIR=${TOMCAT_ROOT}/webapps
TOMCAT_LIB_DIR=${TOMCAT_ROOT}/lib   
   
# expand tomcat    
source ${SCRIPT_DIR}/include/incl-war-unzip.sh

# add alfresco-global.properties
if [ -f ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties.sample ]; then
  mv ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties.sample ${TOMCAT_SHARED_DIR}/classes/alfresco-global.properties
fi

ALFRESCO_WEBAPP=${TOMCAT_WEBAPPS_DIR}/alfresco

###System Configuration###
source ${SCRIPT_DIR}/include/incl-db-setup.sh

###Prepare amp modules###
source ${SCRIPT_DIR}/include/incl-prepare-amps.sh

# TODO: replace by a groovy script that evaluates JSON config
# a kind of groovy json2properties writer
source ${SCRIPT_DIR}/include/incl-config-properties.sh 
source ${SCRIPT_DIR}/include/incl-start-scripts.sh

# TODO: source ${SCRIPT_DIR}/include/incl-install-license.sh
# TODO: 
# TODO: source ${SCRIPT_DIR}/include/incl-config-wqs.sh

# MAYBE: source ${SCRIPT_DIR}/include/incl-config-ldap.sh #

###Content Bootstrap###
# TODO: Load groups
# TODO: Load users
# TODO: Load records and repository content
# TODO: Load Share sites and collaborative content 

echo "Done."