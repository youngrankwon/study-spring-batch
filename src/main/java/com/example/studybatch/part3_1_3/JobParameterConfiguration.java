package com.example.studybatch.part3_1_3;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

/**
 * Test 1. JobParametersBuilder 사용하지 않고 IDE Tool 에서 테스트
 * - JobParameterTest @Component 주석처리
 * - application.yml  batch.job.enabled: true 로 변경
 * - IDE > Add Run Options > Program arguments 에 해당값 입력 후 테스트 ex) name=ran seq(long)=7 date(date)=2025/07/10
 * <p>
 * Test 2. jar 옵션으로 수행
 * - application.yml  batch.job.enabled: true 로 변경
 * - maven package > jar 생성
 * - 명령어로 실행(PowerShell 실행시 따옴표 꼭 넣기) java -jar .\spring-batch-lecture-0.0.1-SNAPSHOT.jar "name=ran" "seq(long)=7" "date(date)=2025/07/11"
 */
@RequiredArgsConstructor
//@Configuration
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job BatchJob() {
        return this.jobBuilderFactory.get("Job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

                        JobParameters jobParameters = stepContribution.getStepExecution().getJobParameters();
                        String name = jobParameters.getString("name");
                        long seq = jobParameters.getLong("seq");
                        Date date = jobParameters.getDate("date");

                        System.out.println("===========================");
                        System.out.println("name: " + name);
                        System.out.println("seq: " + seq);
                        System.out.println("date: " + date);
                        System.out.println("===========================");

                        Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
                        String name2 = (String)jobParameters2.get("name");
                        long seq2 = (long)jobParameters2.get("seq");

                        System.out.println("step1 has executed");

                        return RepeatStatus.FINISHED;
                    }
                })
                .build();

    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 has executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }


}
