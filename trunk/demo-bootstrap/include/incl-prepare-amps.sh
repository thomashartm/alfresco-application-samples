#!/bin/bash

prepare_amps_dir()
{
 # Usage: prepare_amps_dir <path_to_amp_file> <amps or amps-share>
 if [ $# -lt 2 ]; then
    echo "Usage: prepare_amps_dir <path_to_amp_file> <amps or amps-share>"
 fi
 
 echo -n "preparing amp modules ..."
 
 TEMP_DIR="/tmp/${ALF_VERSION}-AMPS-TEMP-$(date '+%s')"
 # make temp folder
 make_dir ${TEMP_DIR} > /dev/null
 
 # make amps folder
 make_dir ${DEST_DIR}/$2 > /dev/null
  
 # unzip amp and copy it to the amps store 
 unzip -q $1 -d ${TEMP_DIR}
 cp ${TEMP_DIR}/*.amp ${DEST_DIR}/$2/
 
 # remove temp folder
 rm -r ${TEMP_DIR}
 
 echo_outcome $?
}

prepare_amps_dir ${SPP_AMP_LOCATION} amps