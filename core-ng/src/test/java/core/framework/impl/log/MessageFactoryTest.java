package core.framework.impl.log;

import core.framework.impl.log.filter.LogFilter;
import core.framework.impl.log.message.ActionLogMessage;
import core.framework.impl.log.message.PerformanceStatMessage;
import core.framework.impl.log.message.StatMessage;
import core.framework.log.Markers;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * @author neo
 */
class MessageFactoryTest {
    @Test
    void stat() {
        StatMessage message = MessageFactory.stat(Map.of("sys_load_avg", 1d), "app");
        assertThat(message.id).isNotNull();
        assertThat(message.stats).containsExactly(entry("sys_load_avg", 1d));
    }

    @Test
    void actionLog() {
        var log = new ActionLog("begin");
        log.action("action");
        log.process(new LogEvent("logger", Markers.errorCode("ERROR_CODE"), LogLevel.WARN, "message", null, null));
        log.track("db", 1000, 1, 2);

        ActionLogMessage message = MessageFactory.actionLog(log, "app", new LogFilter());

        assertThat(message).isNotNull();
        assertThat(message.app).isEqualTo("app");
        assertThat(message.action).isEqualTo("action");
        assertThat(message.errorCode).isEqualTo("ERROR_CODE");
        assertThat(message.traceLog).isNotEmpty();

        PerformanceStatMessage statMessage = message.performanceStats.get("db");
        assertThat(statMessage.totalElapsed).isEqualTo(1000);
        assertThat(statMessage.count).isEqualTo(1);
        assertThat(statMessage.readEntries).isEqualTo(1);
        assertThat(statMessage.writeEntries).isEqualTo(2);
    }
}