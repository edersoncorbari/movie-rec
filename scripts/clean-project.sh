#!/usr/bin/env bash

DIRECTORY=`pwd`

function get-root-dir() {
  if [[ ! ${0} =~ "scripts" ]]; then
    DIRECTORY=`cd -`
  fi
}

function remove-files() {
  find ${DIRECTORY} -name .idea -type d -exec rm -rf {} +

  rm -rf ${DIRECTORY}/project/project
  rm -rf ${DIRECTORY}/project/target
  rm -rf ${DIRECTORY}/target
}

read -p "This script will remove all compilation files. Are you sure? (y/n) " resp
if [ "${resp}" = "y" ]; then
  get-root-dir && remove-files 
fi

exit 0
