#!/bin/bash

echo_success()
{
    echo -e "\\033[78G[\033[1;32mOK\033[0;39m]"
}

echo_failure()
{
    echo -e "\\033[78G[\033[1;31mFAIL\033[0;39m]"
}

echo_outcome()
{
    if test "$1" -ne "0"
    then
	echo_failure
    else
	echo_success
    fi

}

echo_usage()
{
   echo -e "Usage\t\t: `basename $0` /path/to/war-or-zip-bundle "
   echo -e "Usage\t\t: Use default option to choose the default alfresco package"

   exit 1
}

install_cleanup()
{
  if [[ $1 == 'clean' ]]; then
    
      echo "Clean option selected. Starting cleanup work ... "  
      
      if [ -d ${DEST_DIR} ]; then
        echo -n "removing existing ${DEST_DIR} directory"
        rm -R ${DEST_DIR}
        echo_outcome $? 
      fi
      
      if [ -d ${DIR_ROOT} ]; then
        echo -n "removing existing ${DIR_ROOT} directory"
        rm -R ${DIR_ROOT}
        echo_outcome $?   
      fi
      echo -n "Cleanup done."
      echo_outcome $? 
    
  else
    echo "Clean option was not selected."  
  fi   
}

make_dir()
{
  DIR=$1  
  if [ -d ${DIR} ]; then
      echo "${DIR} already exists, aborting!"
      exit 2
  else
      echo "${DIR} does not yet exist, making it"
      mkdir -p ${DIR}
  fi
}