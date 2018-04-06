package settings;

/**
 * This enumerable type lists the various application-specific property types listed in the initial set of properties to
 * be loaded from the workspace properties <code>xml</code> file specified by the initialization parameters.
 *
 * @author Ritwik Banerjee
 * @see vilij.settings.InitializationParams
 */
public enum AppPropertyTypes {

    /* resource files and folders */
    DATA_RESOURCE_PATH,
    RESOURCES_RESOURCE_PATH,

    /* user interface icon file names */
    SCREENSHOT_ICON,
    EMPTY_CHART_IMAGE,
    CONFIG_ICON,
    RUN_ICON,
    DONE_ICON,

    /* tooltips for user interface buttons */
    SCREENSHOT_TOOLTIP,
    READ_ONLY,
    RANDOM_CLUSTER,
    RANDOM_CLASS,
    CONFIG_TOOLTIP,
    RUN_TOOLTIP,
    DONE_TOOLTIP,

    /* error messages */
    RESOURCE_SUBDIR_NOT_FOUND,
    NUMBER_FORMAT_EXCEPTION,
    INVALID_SPACING_ERRORS,
    INVALID_DATA_EXCEPTION,
    IDENTICAL_NAME_EXCEPTION,
    ERROR_DUPLICATE,
    INVALID_DATA_EXCEPT,
    NAME_ERROR_MSG,
    NULL_ERROR,
    SELECTION_ERROR,

    /* application-specific message titles */
    SAVE_UNSAVED_WORK_TITLE,
    CLASSIFICATION,
    CLUSTERING,
    ALG_TYPE,
    MAX_ITER,
    UPDATE_INT,
    CONTINUOUS,
    CONFIG_WINDOW_TITLE,
    NUM_LABELS,

    /* application-specific messages */
    SAVE_UNSAVED_WORK,
    EXIT_WHILE_RUNNING_WARNING ,
    ERROR_LINE,
    DATA_OVERFLOW_1,
    DATA_OVERFLOW_2,
    

    /* application-specific parameters */
    DATA_FILE_EXT,
    DATA_FILE_EXT_DESC,
    SCREENSHOT_EXT_DESC,
    SCREENSHOT_EXT,
    TEXT_AREA,
    SPECIFIED_FILE,
    DATA_VISUALIZATION,
    DISPLAY,
    
    /*application-style strings*/
    DONE,
    EDIT,
    
}
