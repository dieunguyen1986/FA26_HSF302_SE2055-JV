package fu.hr;

import fu.hr.entity.Jobs;
import fu.hr.service.JobService;
import fu.hr.service.JobServiceImpl;

/**
 * Hello world!
 *
 */
public class App {
    private static JobService jobService = new JobServiceImpl();

    public static void main(String[] args) {
        Jobs jobs = Jobs.builder()
                .jobTitle("Java Senior Dev")
                .minSalary(1000d)
                .maxSalary(5000d)
                .build();

        try {
            System.out.println("Save " + jobService.save(jobs).getJobTitle() + " to DB success!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
