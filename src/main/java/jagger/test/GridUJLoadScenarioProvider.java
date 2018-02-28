package jagger.test;

import com.griddynamics.jagger.engine.e1.collector.DefaultResponseValidatorProvider;
import com.griddynamics.jagger.engine.e1.collector.JHttpResponseStatusValidatorProvider;
import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileRps;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.RequestsPerSecond;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaBackground;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaDuration;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GridUJLoadScenarioProvider {
    /**
     * Create scenario from 3 tests groups with the following parameters:

     2 users 5 iterations
     3 user for 2 minutes with 15 seconds delay between invocations starting by 1 user each 20 seconds
     2 users in parallel: 1 user for 3 minutes with 20 seconds delay between invication, second with 15 seconds delay working in background
     */

    @Bean
    public JParallelTestsGroup gridU_2users5iterations() {
        JTestDefinition jTestDefinition = JTestDefinition.builder(Id.of("2users_5iterations_td"), new EndpointsProvider())
                .withQueryProvider(new QueriesProvider())
                .withInvoker(new MyQueryParamsAwareInvoker())
                .addValidator(JHttpResponseStatusValidatorProvider.of(200))
                .addValidator(new HeaderResponseValidatorProvider())
                .addValidator(new BodyResponseValidatorProvider("response-headers","application/json"))
                .addValidator(new BodyResponseValidatorProvider("get","http://httpbin.org/get"))
                .addValidator(new BodyResponseValidatorProvider("xml","A SAMPLE set of slides"))
                .addValidator(DefaultResponseValidatorProvider.of(NotNullResponseValidator.class))
                .addListener(new BodySizeInvocationListener())
                .build();

        JLoadProfile jLoadProfileRps = JLoadProfileRps
                .builder(RequestsPerSecond.of(1))
                .withMaxLoadThreads(1)
                .withWarmUpTimeInMilliseconds(10000)
                .build();

        JTerminationCriteria jTerminationCriteria = JTerminationCriteriaIterations.of(IterationsNumber.of(5), MaxDurationInSeconds.of(9999));

        JLoadTest jLoadTest1 = JLoadTest.builder(Id.of("2users5iterations_lt1"), jTestDefinition, jLoadProfileRps, jTerminationCriteria).build();
        JLoadTest jLoadTest2 = JLoadTest.builder(Id.of("2users5iterations_lt2"), jTestDefinition, jLoadProfileRps, jTerminationCriteria).build();

        return JParallelTestsGroup.builder(Id.of("2users5iterations_ptg"), jLoadTest1, jLoadTest2).build();
    }

    @Bean
    public JParallelTestsGroup gridU_3users2minutes() {

        JTestDefinition jTestDefinition = JTestDefinition
                .builder(Id.of("3users2minutes_td"), new EndpointsProvider())
                .withQueryProvider(new QueriesProvider())
                .withInvoker(new MyQueryParamsAwareInvoker())
                .addValidator(JHttpResponseStatusValidatorProvider.of(200))
                .addValidator(new HeaderResponseValidatorProvider())
                .addValidator(new BodyResponseValidatorProvider("response-headers","application/json"))
                .addValidator(new BodyResponseValidatorProvider("get","http://httpbin.org/get"))
                .addValidator(new BodyResponseValidatorProvider("xml","A SAMPLE set of slides"))
                .addValidator(DefaultResponseValidatorProvider.of(NotNullResponseValidator.class))
                .addListener(new BodySizeInvocationListener())
                .build();

        JLoadProfileUsers user1 = JLoadProfileUsers.builder(NumberOfUsers.of(1))
                .withStartDelayInSeconds(0)
                .build();

        JLoadProfileUsers user2 = JLoadProfileUsers.builder(NumberOfUsers.of(1))
                .withStartDelayInSeconds(20)
                .build();

        JLoadProfileUsers user3 = JLoadProfileUsers.builder(NumberOfUsers.of(1))
                .withStartDelayInSeconds(40)
                .build();

        JLoadProfileUserGroups group = JLoadProfileUserGroups
                .builder(user1, user2, user3)
                .withDelayBetweenInvocationsInMilliseconds(15000)
                .build();

        JTerminationCriteria jTerminationCriteria = JTerminationCriteriaDuration.of(DurationInSeconds.of(120));

        JLoadTest jLoadTest = JLoadTest.builder(Id.of("3users2minutes_lt"), jTestDefinition, group, jTerminationCriteria).build();

        return JParallelTestsGroup.builder(Id.of("3users2minutes_ptg"), jLoadTest).build();
    }

    @Bean
    public JParallelTestsGroup gridU_2userInParallel() {

        JTestDefinition jTestDefinition = JTestDefinition
                .builder(Id.of("2userInParallel_td"), new EndpointsProvider())
                .withQueryProvider(new QueriesProvider())
                .withInvoker(new MyQueryParamsAwareInvoker())
                .addValidator(JHttpResponseStatusValidatorProvider.of(200))
                .addValidator(new HeaderResponseValidatorProvider())
                .addValidator(new BodyResponseValidatorProvider("response-headers","application/json"))
                .addValidator(new BodyResponseValidatorProvider("get","http://httpbin.org/get"))
                .addValidator(new BodyResponseValidatorProvider("xml","A SAMPLE set of slides"))
                .addValidator(DefaultResponseValidatorProvider.of(NotNullResponseValidator.class))
                .addListener(new BodySizeInvocationListener())
                .build();

        JLoadProfileUsers user1 = JLoadProfileUsers.builder(NumberOfUsers.of(1))
                .withStartDelayInSeconds(0)
                .build();

        JLoadProfileUserGroups group1 = JLoadProfileUserGroups.builder(user1)
                .withDelayBetweenInvocationsInMilliseconds(20000)
                .build();

        JTerminationCriteria jTerminationCriteria1 = JTerminationCriteriaDuration.of(DurationInSeconds.of(180));

        JLoadTest jLoadTest1 = JLoadTest.builder(Id.of("2userInParallel_lt1"), jTestDefinition, group1, jTerminationCriteria1).build();

        JLoadProfileUsers user2 = JLoadProfileUsers.builder(NumberOfUsers.of(1))
                .withStartDelayInSeconds(0)
                .build();

        JLoadProfileUserGroups group2 = JLoadProfileUserGroups.builder(user2)
                .withDelayBetweenInvocationsInMilliseconds(15000)
                .build();

        JTerminationCriteria jTerminationCriteria2 = JTerminationCriteriaBackground.getInstance();

        JLoadTest jLoadTest2 = JLoadTest.builder(Id.of("2userInParallel_lt2"), jTestDefinition, group2, jTerminationCriteria2).build();

        return JParallelTestsGroup.builder(Id.of("2userInParallel_ptg"), jLoadTest1, jLoadTest2).build();
    }

    @Bean
    public JLoadScenario runScenarios(){
        return JLoadScenario.builder(Id.of("gridUScenario"), gridU_2users5iterations(), gridU_3users2minutes(), gridU_2userInParallel()).build();
    }
}
