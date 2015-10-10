package bp.state;

public enum StateType {

    SELECT,
    TASK,
    USER_TASK,
    SYSTEM_TASK,
    EDGE,
    CONDITIONAL_EDGE,
    GATEWAY,
    LANE,
    MOVE,
    MOVE_EDGE,
    RESIZE,
    START_EVENT,
    TIMER_START_EVENT,
    CONDITIONAL_START_EVENT,
    MESSAGE_START_EVENT,
    SIGNAL_START_EVENT,
    ERROR_START_EVENT,
    END_EVENT,
    MESSAGE_END_EVENT,
    ERROR_END_EVENT,
    SIGNAL_END_EVENT,
    TIMER_CATCH_EVENT,
    CONDITIONAL_CATCH_EVENT,
    MESSAGE_CATCH_EVENT,
    SIGNAL_CATCH_EVENT,
    LINK_CATCH_EVENT,
    MESSAGE_THROW_EVENT,
    SIGNAL_THROW_EVENT,
    LINK_THROW_EVENT,
    MESSAGE_ACTIVITY_EVENT,
    TIMER_ACTIVITY_EVENT,
    CONDITIONAL_ACTIVITY_EVENT,
    SIGNAL_ACTIVITY_EVENT,
    ERROR_ACTIVITY_EVENT;

}
