#!/bin/bash

set -e

cd ..

mvn compile
if [ $? -ne 0 ]; then
  echo "Maven compile failed"
  exit 1
fi

mvn exec:java -Dexec.mainClass="com.example.XsdToSingletonGenerator"
if [ $? -ne 0 ]; then
  echo "Maven exec:java failed"
  exit 1
fi
