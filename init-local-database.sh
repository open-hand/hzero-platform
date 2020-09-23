#!/usr/bin/env bash
mkdir -p target
if [ ! -f target/choerodon-tool-liquibase.jar ]
then
    curl https://nexus.choerodon.com.cn/repository/choerodon-release/io/choerodon/choerodon-tool-liquibase/0.8.1.RELEASE/choerodon-tool-liquibase-0.8.1.RELEASE.jar -o target/choerodon-tool-liquibase.jar
fi

java -Dspring.datasource.url="jdbc connection url" \
	 -Dspring.datasource.username=username \
	 -Dspring.datasource.password=password \
	 -Ddata.drop=false -Ddata.init=init \
	 -Ddata.dir=src/main/resources \
	 -jar target/choerodon-tool-liquibase.jar
