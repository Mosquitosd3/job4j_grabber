package ru.job4j.quartz;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    public static void main(String[] args) {
        Properties cfg = cfg();
        try (Connection connection = getConnect(cfg)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connect", connection);
            JobDetail job = newJob(Rabbit.class).usingJobData(data).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    private static void insert(Connection connection, long date)  {
       try (PreparedStatement statement = connection.prepareStatement("insert into rabbit_s.rabbit(created_data) values(?)")) {
           statement.setLong(1, date);
           statement.execute();
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
    }

    private static Connection getConnect(Properties properties) throws SQLException {
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = properties.getProperty("url");
        String login = properties.getProperty("login");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, login, password);
    }

    private static Properties cfg() {
        Properties properties = new Properties();
        ClassLoader loader = AlertRabbit.class.getClassLoader();
        try (InputStream io = loader.getResourceAsStream("rabbit.properties")) {
           properties.load(io);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connect");
            insert(connection, System.currentTimeMillis());
        }
    }
}
