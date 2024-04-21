package com.example;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LogParserTest extends TestCase {

    public LogParserTest(String testName) {
        super(testName);
    }

    // Test parsing of log time
    public void testParseLogTime() throws ParseException {
        String logLine = "[2024-02-23T21:32:41.489+0000] [Payara 6.2024.3-SNAPSHOT] [SEVERE] [] [com.ibm.jbatch.container.impl.ChunkStepControllerImpl] [tid: _ThreadID=173 _ThreadName=concurrent/__defaultManagedExecutorService-managedThreadFactory-Thread-1] [timeMillis: 1708723961489] [levelValue: 1000] [[Failure in Read-Process-Write Loop]]";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2024-02-23T21:32:41.489");
        assertEquals(expectedDate, LogParser.parseLogTime(logLine));
    }

    public void testGetFirstLineOfMessage() {
        String logLine = "[2024-02-23T21:32:41.489+0000] [Payara 6.2024.3-SNAPSHOT] [SEVERE] [] [com.ibm.jbatch.container.impl.ChunkStepControllerImpl] [tid: _ThreadID=173 _ThreadName=concurrent/__defaultManagedExecutorService-managedThreadFactory-Thread-1] [timeMillis: 1708723961489] [levelValue: 1000] [[Failure in Read-Process-Write Loop]]";
        String expectedFirstLine = "Failure in Read-Process-Write Loop";
        assertEquals(expectedFirstLine, LogParser.getFirstLineOfMessage(logLine));
    }
    
    public void testGetExceptionType() {
        String logLine = "[2024-02-23T21:32:41.735+0000] [Payara 6.2024.3-SNAPSHOT] [SEVERE] [] [com.ibm.jbatch.container.impl.ChunkStepControllerImpl] [tid: _ThreadID=173 _ThreadName=concurrent/__defaultManagedExecutorService-managedThreadFactory-Thread-1] [timeMillis: 1708723961735] [levelValue: 1000] [[\n"
    + "            Failure in Read-Process-Write Loop\n"
    + "          com.ibm.jbatch.container.exception.BatchContainerRuntimeException: java.lang.Exception: fail on purpose on idx = 12\n"
    + "              at com.ibm.jbatch.container.impl.ChunkStepControllerImpl.readItem(ChunkStepControllerImpl.java:354)\n"
    + "              at com.ibm.jbatch.container.impl.ChunkStepControllerImpl.readAndProcess(ChunkStepControllerImpl.java:255)\n"
    + "              at com.ibm.jbatch.container.impl.ChunkStepControllerImpl.invokeChunk(ChunkStepControllerImpl.java:622)\n"
    + "              at com.ibm.jbatch.container.impl.ChunkStepControllerImpl.invokeCoreStep(ChunkStepControllerImpl.java:763)\n"
    + "              at com.ibm.jbatch.container.impl.BaseStepControllerImpl.execute(BaseStepControllerImpl.java:149)\n"
    + "              at com.ibm.jbatch.container.impl.ExecutionTransitioner.doExecutionLoop(ExecutionTransitioner.java:112)\n"
    + "              at com.ibm.jbatch.container.impl.JobThreadRootControllerImpl.originateExecutionOnThread(JobThreadRootControllerImpl.java:110)\n"
    + "              at com.ibm.jbatch.container.util.BatchWorkUnit.run(BatchWorkUnit.java:80)\n"
    + "              at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)\n"
    + "              at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)\n"
    + "              at org.glassfish.enterprise.concurrent.internal.ManagedFutureTask.run(ManagedFutureTask.java:119)\n"
    + "              at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)\n"
    + "              at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)\n"
    + "              at java.base/java.lang.Thread.run(Thread.java:829)\n"
    + "              at org.glassfish.enterprise.concurrent.ManagedThreadFactoryImpl$ManagedThread.run(ManagedThreadFactoryImpl.java:322)\n"
    + "          Caused by: java.lang.Exception: fail on purpose on idx = 12\n"
    + "              at com.ibm.jbatch.tck.artifacts.specialized.DoSomethingArrayItemReaderImpl.readItem(DoSomethingArrayItemReaderImpl.java:137)\n"
    + "              at com.ibm.jbatch.tck.artifacts.specialized.DoSomethingArrayItemReaderImpl.readItem(DoSomethingArrayItemReaderImpl.java:33)\n"
    + "              at com.ibm.jbatch.container.artifact.proxy.ItemReaderProxy.readItem(ItemReaderProxy.java:68)\n"
    + "              at com.ibm.jbatch.container.impl.ChunkStepControllerImpl.readItem(ChunkStepControllerImpl.java:321)\n"
    + "              ... 14 more\n"
    + "          ]]";

        String expectedExceptionType = "com.ibm.jbatch.container.exception.BatchContainerRuntimeException";
        assertEquals(expectedExceptionType, LogParser.getExceptionType(logLine));
    }
    

    public void testReadLogFile() {
        List<String> logEntries = LogParser.readLogFile("log.txt");
        assertNotNull(logEntries);
        assertTrue(logEntries.size() > 0);
    }

    public void testGenerateHTMLReport() {
        List<String> severeLogs = Arrays.asList("Log 1", "Log 2", "Log 3");
        Map<Long, List<String>> groupedSevereLogs = new HashMap<>();
        groupedSevereLogs.put(1645643561489L, Arrays.asList("Group 1 Log 1", "Group 1 Log 2"));
        groupedSevereLogs.put(1645643561490L, Arrays.asList("Group 2 Log 1", "Group 2 Log 2"));
        // Call generateHTMLReport method with test data
        LogParser.generateHTMLReport(severeLogs, groupedSevereLogs);
        // Verify that the report file exists
        File reportFile = new File("report.html");
        assertTrue(reportFile.exists());
    }
}

