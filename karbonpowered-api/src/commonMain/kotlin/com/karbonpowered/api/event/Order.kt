package com.karbonpowered.api.event

enum class Order {

    /**
     * The order point of PRE handles setting up things that need to be done
     * before other things are handled.
     */
    PRE,

    /**
     * The order point of AFTER_PRE handles things that need to be done after
     * PRE.
     */
    AFTER_PRE,

    /**
     * The order point of FIRST handles cancellation by protection plugins for
     * informational responses.
     */
    FIRST,

    /**
     * The order point of EARLY handles standard actions that need to be done
     * before other plugins.
     */
    EARLY,

    /**
     * The order point of DEFAULT handles just standard event handlings, you
     * should use this unless you know you need otherwise.
     */
    DEFAULT,

    /**
     * The order point of LATE handles standard actions that need to be done
     * after other plugins.
     */
    LATE,

    /**
     * The order point of LAST handles last minute cancellations by protection
     * plugins.
     */
    LAST,

    /**
     * The order point of BEFORE_POST handles preparation for things needing
     * to be done in post.
     */
    BEFORE_POST,

    /**
     * The order point of POST handles last minute things and monitoring
     * of events for rollback or logging.
     */
    POST

}