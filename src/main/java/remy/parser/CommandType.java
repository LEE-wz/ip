package remy.parser;

/**
 * The CommandType enum is a data type to define a fixed set of instructions for the chatbot to follow.
 * Each CommandType will result in different actions performed by the chatbot.
 *
 * @author LEE-wz
 */
public enum CommandType {
    BYE,
    LIST,
    DELETE,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    UNKNOWN;
}
