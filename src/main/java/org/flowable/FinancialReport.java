package org.flowable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

public class FinancialReport {

	public static void main(String[] args) {
		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
				.setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=1")
				.setJdbcUsername("sa")
				.setJdbcPassword("")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		ProcessEngine processEngine = cfg.buildProcessEngine();
		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeployment()
				  .addClasspathResource("FinancialReportProcess.bpmn20.xml")
				  .deploy();
		System.out.println("deployment id: " + deployment.getId());
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");
		System.out.println("instance id: " + processInstance.getId());
		
		TaskService taskService = processEngine.getTaskService();
//		List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("kermit").list();
//		for(int i = 0; i < tasks.size(); i++){
//			System.out.println((i+1)+") " + tasks.get(i).getName());
//		}
		
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
		for(int i = 0; i < tasks.size(); i++){
			System.out.println((i+1)+") " + tasks.get(i).getName());
		}
		taskService.complete(tasks.get(0).getId());	
		
		tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for(int i = 0; i < tasks.size(); i++){
			System.out.println((i+1)+") " + tasks.get(i).getName());
		}
		taskService.complete(tasks.get(0).getId());	
		
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstance historicProcessInstance =
		historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
		System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
		
	}
}