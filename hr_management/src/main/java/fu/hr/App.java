package fu.hr;

import fu.hr.entity.Jobs;
import fu.hr.service.JobService;
import fu.hr.service.JobServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        // get bean
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("fu.hr");

        JobService jobService = applicationContext.getBean("jobService", JobServiceImpl.class);

        Jobs job1 = applicationContext.getBean(Jobs.class);
        Jobs job2 = applicationContext.getBean(Jobs.class);

        job1.setJobTitle("Tester");

        System.out.println(job1.getJobTitle());
        System.out.println(job2.getJobTitle());

        job2.setJobTitle("PM");
        System.out.println(job1.getJobTitle());
        System.out.println(job2.getJobTitle());


        Jobs job = Jobs.builder()
                .jobTitle("BA Senior")
                .minSalary(1000d)
                .maxSalary(5000d)
                .build();

        try {
            // Create a new job
//            System.out.println("Save " + jobService.save(job).getJobTitle() + " to DB success!");

            // Get all job to display
            jobService.findAll().forEach((j) -> {
                System.out.println(j.toString());
            });
        } catch (Exception e) {

            System.err.println(e.getMessage());
        }

    }
}
