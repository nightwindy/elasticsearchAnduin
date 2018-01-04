package com.anduin.mybatisgen;

import java.io.File;


public class MyBatisGenConst {

    // 包名
    public static final String MAIN_PACKAGE = "com.anduin.dal";
    public static final String DO_PACKAGE = "com.anduin.dal.model";
    public static final String QUERY_PACKAGE="com.anduin.dal.query";
    public static final String MAPPER_PACKAGE = "com.anduin.dal.mapper";
    public static final String MANAGER_PACKAGE= "com.anduin.dal.manager";
    public static final String MANAGER_IMPL_PACKAGE= MANAGER_PACKAGE+".impl";

    public static final String MAPPER_EXT_PACKAGE = "com.anduin.dal.mapper.ext";
    public static final String TABLE_PREFIX = "cc_";
    public static final String QUERY_PREFIX = "Query";
    public static final String MAPPER_SUFFIX = "Mapper";
    public static final String MANAGER_SUFFIX = "Manager";
    public static final String MANAGER_IMPL_SUFFIX = "ManagerImpl";
    public static final String MAPPER_EXT_SUFFIX = "ExtMapper";
    public static final String DO_SUFFIX = "DO";

    // 工作目录
    public static final String WORK_DIR = System.getProperty("user.dir") + File.separator;
    // mapper xml 输出目录
    public static final String MAPPER_XML_DIR = WORK_DIR + "anduin-dal/src/main/resources/mapper/";
    // mapper-ext xml 输出目录
    public static final String MAPPER_EXT_XML_DIR = WORK_DIR + "anduin-dal/src/main/resources/mapper/ext/";
    // do/model 输出目录
    public static final String MAPPER_DO_DIR = WORK_DIR + "anduin-dal/src/main/java/com/windy/anduin/dal/model";
    // query 输出目录
    public static final String MAPPER_QUERY_DIR = WORK_DIR + "anduin-dal/src/main/java/com/windy/anduin/dal/query";
    // mapper java 输出目录
    public static final String MAPPER_JAVA_DIR = WORK_DIR + "anduin-dal/src/main/java/com/windy/anduin/dal/mapper";

    public static final String MANAGER_JAVA_DIR = WORK_DIR + "anduin-dal/src/main/java/com/windy/anduin/dal/manager";
    public static final String MANAGER_IMPL_JAVA_DIR =  MANAGER_JAVA_DIR +"/impl";



    // mapper-ext java 输出目录
    public static final String MAPPER_EXT_JAVA_DIR = WORK_DIR + "anduin-dal/src/main/java/com/windy/anduin/dal/mapper/ext";
    // java数据对象类模板
    public static final String DO_TEMPLATE = ClassLoader.getSystemResource("template/do.txt").getPath();//  "resources/template/do.txt";
    // query 模板
    public static final String QUERY_TEMPLATE = ClassLoader.getSystemResource("template/query.txt").getPath();
    // sqlmap模板
    public static final String SQLMAP_TEMPLATE = ClassLoader.getSystemResource("template/sqlmap.txt").getPath();
    // mapper模板
    public static final String MAPPER_TEMPLATE = ClassLoader.getSystemResource("template/mapper.txt").getPath();
    // manager模板
    public static final String MANAGER_TEMPLATE = ClassLoader.getSystemResource("template/manager.txt").getPath();
    // manager impl 模板
    public static final String MANAGER_IMPL_TEMPLATE = ClassLoader.getSystemResource("template/managerImpl.txt").getPath();

    // mapper-ext模板
    public static final String MAPPER_EXT_TEMPLATE = ClassLoader.getSystemResource("template/mapper-ext.txt").getPath();
    // sqlmap-ext模板
    public static final String SQLMAP_EXT_TEMPLATE = ClassLoader.getSystemResource("template/sqlmap-ext.txt").getPath();

    public static final String COMMON_COLUMN_STR = "gmt_create,gmt_modified,";

    // jdbc result set metadata collumn name
    public static final String RSMD_COLUMN_NAME = "rsmdColumnName";
    public static final String RSMD_COLUMN_CLASS_NAME = "columnClassName";
    public static final String RSMD_COLUMN_TYPE_NAME = "columnTypeName";
    public static final String RSMD_COLUMN_PRECISION = "Precision";
    public static final String RSMD_COLUMN_SCALE = "Scale";
    public static final String RSMD_COLUMN_PRIMARY_KEY="PrimaryKey";
    public static final String RSMD_COLUMN_REMARKS="Remarks";

    public static final String COLUMN_NAME="COLUMN_NAME";

    // velocity param
    public static final String VP_LIST = "list";
    public static final String VP_QUERY_PREFIX = "queryPrefix";
    public static final String VP_DO_SUFFIX = "doSuffix";
    public static final String VP_MAPPER_SUFFIX = "mapperSuffix";
    public static final String VP_MANAGER_SUFFIX = "managerSuffix";

    public static final String VP_MANAGER_IMPL_SUFFIX = "managerImplSuffix";
    public static final String VP_MAPPER_EXT_SUFFIX = "extMapperSuffix";
    public static final String VP_CLASS_NAME = "className";
    public static final String VP_MAPPER_PROPERTY_NAME = "mapperPropertyName";
    public static final String VP_MAIN_PACKAGE = "mainPackage";
    public static final String VP_DO_PACKAGE = "doPackage";
    public static final String VP_QUERY_PACKAGE = "queryPackage";
    public static final String VP_MAPPER_PACKAGE = "mapperPackage";
    public static final String VP_MANAGER_PACKAGE = "managerPackage";
    public static final String VP_MANAGER_IMPL_PACKAGE = "managerImplPackage";
    public static final String VP_MAPPER_EXT_PACKAGE = "mapperExtPackage";
    public static final String VP_JAVA_TYPE = "javaType";
    public static final String VP_PROP_NAME = "propName";
    public static final String VP_GET_METHOD = "getMethod";

    public static final String VP_SET_METHOD = "setMethod";
    public static final String VP_COLUMN_NAME = "columnName";
    public static final String VP_COLUMN_REMARKS = "columnRemarks";
    public static final String VP_TABLE_NAME = "tableName";
    public static final String VP_JDBC_TYPE = "jdbcType";
    public static final String VP_COLS = "cols";
    public static final String VP_COLS_WITHOUT_COMMON_COLUMNS = "colsWithoutCommColumns";
    public static final String VP_SERIAL_VERSION_UID = "serialVersionUID";

    public static final String VP_SERIAL_VERSION_UID2 = "serialVersionUID2";
    public static final String VP_PRIMARY_KEY = "primaryKey";
    public static final String VP_PROP_PRIMARY_KEY="propPrimaryKey";

    //分库分表 表后缀regex
    public static final String SHARDING_SUFFIX_REG = "_[\\d]{4}";

}
