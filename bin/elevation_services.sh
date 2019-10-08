#!/bin/bash
#
# Startup script for a spring boot project
#
# chkconfig: - 84 16
# description: elevation_services

# Source function library.
[ -f "/etc/rc.d/init.d/functions" ] && . /etc/rc.d/init.d/functions
[ -z "$JAVA_HOME" -a -x /etc/profile.d/java.sh ] && . /etc/profile.d/java.sh

if [ -z "$JAVA_HOME" ] ; then
    JAVA_HOME=/usr/java/jdk1.8.0
fi

JAVA_OPTS="-XX:MetaspaceSize=1g -XX:MaxMetaspaceSize=2g -XX:+UseConcMarkSweepGC -Xms1g -Xmx2g"

# the name of the project, will also be used for the war file, log file, ...
PROJECT_NAME=elevation_services
# the user which should run the service
SERVICE_USER=tomcat
# base directory for the spring boot jar
SPRINGBOOTAPP_HOME=/var/local/$PROJECT_NAME
export SPRINGBOOTAPP_HOME

# the spring boot war-file
SPRINGBOOTAPP_WAR="$SPRINGBOOTAPP_HOME/elevation_services-1.0.0.jar"

# java executable for spring boot app, change if you have multiple jdks installed
SPRINGBOOTAPP_JAVA=$JAVA_HOME/bin/java
DATE=`date +%Y-%m-%d`

# spring bootlog-file
LOG="/var/log/applications/$PROJECT_NAME.log"
APPLICATION_LOG="/var/log/applications/$PROJECT_NAME.$DATE.*.log"
LOCK="/var/lock/subsys/$PROJECT_NAME"

RETVAL=0

pid_of_spring_boot() {
    pgrep -f "java.*$PROJECT_NAME"
}

start() {
    su $SERVICE_USER -c "truncate -s 0 $LOG"
    [ -e "$LOG" ] && cnt=`wc -l "$LOG" | awk '{ print $1 }'` || cnt=1

    echo -n $"Starting $PROJECT_NAME: "

    cd "$SPRINGBOOTAPP_HOME"
    su $SERVICE_USER -c "nohup $SPRINGBOOTAPP_JAVA $JAVA_OPTS -jar \"$SPRINGBOOTAPP_WAR\"  >> \"$LOG\" 2>&1 &"
    # Wait for the application to create the output log file.
    sleep 5 
    #TEST= `/bin/ls -1tr $APPLICATION_LOG | tail -n 1`
    #cho $TEST
    while { pid_of_spring_boot > /dev/null ; } &&
        ! { tail --lines=+$cnt "`/bin/ls -1tr $APPLICATION_LOG | tail -n 1`" | grep -q ' Started OpenAPI2SpringBoot' ; } ; do
        sleep 1
    done

    pid_of_spring_boot > /dev/null
    RETVAL=$?
    [ $RETVAL = 0 ] && success $"$STRING" || failure $"$STRING"
    echo

    [ $RETVAL = 0 ] && touch "$LOCK"
}

stop() {
    echo -n "Stopping $PROJECT_NAME: "

    pid=`pid_of_spring_boot`
    [ -n "$pid" ] && kill $pid
    RETVAL=$?
    cnt=10
    while [ $RETVAL = 0 -a $cnt -gt 0 ] &&
        { pid_of_spring_boot > /dev/null ; } ; do
            sleep 1
            ((cnt--))
    done

    [ $RETVAL = 0 ] && rm -f "$LOCK"
    [ $RETVAL = 0 ] && success $"$STRING" || failure $"$STRING"
    echo
}

status() {
    pid=`pid_of_spring_boot`
    if [ -n "$pid" ]; then
        echo "$PROJECT_NAME (pid $pid) is running..."
        return 0
    fi
    if [ -f "$LOCK" ]; then
        echo $"${base} dead but subsys locked"
        return 2
    fi
    echo "$PROJECT_NAME is stopped"
    return 3
}

# See how we were called.
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    status)
        status
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
esac

exit $RETVAL
