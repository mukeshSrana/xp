package com.enonic.wem.api.index;

public final class IndexPaths
{
    private static final String DIVIDER = ".";

    public static final String ID_KEY = "_id";

    public static final IndexDocumentItemPath CREATED_TIME_PATH = IndexDocumentItemPath.from( "_createdtime" );

    public static final IndexDocumentItemPath NAME_PATH = IndexDocumentItemPath.from( "_name" );

    public static final IndexDocumentItemPath CREATOR_PATH = IndexDocumentItemPath.from( "_creator" );

    public static final String MODIFIED_TIME_KEY = "_modifiedTime";

    public static final IndexDocumentItemPath MODIFIED_TIME_PATH = IndexDocumentItemPath.from( MODIFIED_TIME_KEY );

    public static final IndexDocumentItemPath MODIFIER_PATH = IndexDocumentItemPath.from( "_modifier" );

    public static final String PARENT_PATH_KEY = "_parentpath";

    public static final IndexDocumentItemPath PARENT_PATH = IndexDocumentItemPath.from( PARENT_PATH_KEY );

    public static final String PATH_KEY = "_path";

    public static final IndexDocumentItemPath PATH_PATH = IndexDocumentItemPath.from( PATH_KEY );

    public static final String MANUAL_ORDER_VALUE_KEY = "_manualordervalue";

    public static final IndexDocumentItemPath MANUAL_ORDER_VALUE_PATH = IndexDocumentItemPath.from( MANUAL_ORDER_VALUE_KEY );

    public static final String VERSION_KEY = "_versionkey";

    public static final IndexDocumentItemPath VERSION_KEY_PATH = IndexDocumentItemPath.from( VERSION_KEY );

    private static final String PERMISSIONS_ROOT = "_permissions";

    private static final String PERMISSIONS_READ_KEY = "read";

    private static final String PERMISSIONS_CREATE_KEY = "create";

    private static final String PERMISSIONS_MODIFY_KEY = "modify";

    private static final String PERMISSIONS_DELETE_KEY = "delete";

    private static final String PERMISSIONS_PUBLISH_KEY = "publish";

    private static final String PERMISSIONS_READ_PERMISSIONS_KEY = "readpermissions";

    public static final String PERMISSIONS_WRITE_PERMISSIONS_KEY = "writepermissions";

    public static final IndexDocumentItemPath PERMISSIONS_READ_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_READ_KEY );

    public static final IndexDocumentItemPath PERMISSIONS_CREATE_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_CREATE_KEY );

    public static final IndexDocumentItemPath PERMISSIONS_MODIFY_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_MODIFY_KEY );

    public static final IndexDocumentItemPath PERMISSIONS_DELETE_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_DELETE_KEY );

    public static final IndexDocumentItemPath PERMISSIONS_PUBLISH_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_PUBLISH_KEY );

    public static final IndexDocumentItemPath PERMISSIONS_READ_PERMISSIONS_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_READ_PERMISSIONS_KEY );

    public static final IndexDocumentItemPath PERMISSIONS_WRITE_PERMISSIONS_PATH =
        IndexDocumentItemPath.from( PERMISSIONS_ROOT + DIVIDER + PERMISSIONS_WRITE_PERMISSIONS_KEY );

}
