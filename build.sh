#! /bin/bash

if [ -z $1 ]
then
  PACHYNAME="pachyderm"
  BUILDTYPE="Apache"
else
  if [ $1 = "Apache" -o $1 = "Tomcat" ]
  then
    PACHYNAME="pachyderm"
    BUILDTYPE=$1
  else
    PACHYNAME=$1
    BUILDTYPE="Apache"
  fi

  if [ $2 ]
  then
    BUILDTYPE=$2
  fi

fi

if [ $BUILDTYPE = "Tomcat" ]
then
  echo "Building $PACHYNAME.war ## for $BUILDTYPE"
  servletDeployment="true"
  webXML="true"
else
  echo "Building $PACHYNAME.woa ## for $BUILDTYPE"
  servletDeployment="false"
  webXML="false"
fi

umask u=rwx,g=rwx,o=rx

# frameworks -----------------------------------------------------------------------------

ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Frameworks/Apollo/CoreServices/build.xml install
ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Frameworks/Apollo/DataServices/build.xml install
ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Frameworks/SimpleAuthenticationSupport/build.xml install

# plug-ins -------------------------------------------------------------------------------

ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Plugins/AssetDBSupport/build.xml install

# frameworks -----------------------------------------------------------------------------

ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Frameworks/PXFoundation/build.xml install
ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Frameworks/Apollo/AppServices/build.xml install

# plug-ins -------------------------------------------------------------------------------

#ant -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source -f Plugins/OKIOSIDDBSupport/build.xml install

# application ----------------------------------------------------------------------------

ant -DPACHYNAME=$PACHYNAME -DservletDeployment=$servletDeployment -DwebXML=$webXML \
    -Dant.build.javac.target=1.5 -Dant.build.javac.source=1.5 -Ddebug=true -Ddebuglevel=lines,vars,source \
    -f Applications/Pachyderm3/build.xml install

# extras ---------------------------------------------------------------------------------

#TODO .. package up the Component and Template descriptions, and the fixed web assets ... 
#        store these in the Resources for first-time deployment ...

# rm -f /tmp/p30-web-assets.zip
# zip -r --exclude=*.svn* --exclude=*.DS_Store* /tmp/p30-web-assets Resources/PachyRepo22
