package com.auto.tools.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;

import com.auto.tools.TestManager;
import com.auto.tools.models.TestCaseStatus;
import com.auto.tools.ui.components.listener.ExportButtonMouseListener;
import com.auto.tools.ui.constant.TestCaseDataTableColumns;

public class SummaryChartPane extends JPanel{

	private DeviceControlPane deviceControlPane;
	private TestCaseDataTable table;

	public SummaryChartPane(DeviceControlPane pane, JTable table)
	{
		this.deviceControlPane = pane;
		this.table = (TestCaseDataTable)table;

		initComponents();
		updatePaneData();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(true)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					java.awt.EventQueue.invokeLater(new Runnable(){

						@Override
						public void run() {
							updatePaneData();
						}});
				}
			}

		}).start();
	}

	public void updatePaneData()
	{
		TableModel dataTableModel = table.getModel();
		
		int total = dataTableModel.getRowCount();
		if(total == 1 
				&& dataTableModel.getValueAt(0, TestCaseDataTableColumns.SCRIPT_NAME.INDEX).toString().isEmpty())
		{
			total = 0;
		}

		int passed = 0;
		int failed = 0;
		int aborted = 0;
		int cancelled = 0;
		int na = 0;
		int pending = 0;
		int running = 0;
		int warning = 0;
		int timeout = 0;

		// Get data from TestCase table
		TestCaseStatus status = null;
		for(int i = 0; i < total; i++)
		{
			status = TestCaseStatus.valueOf(dataTableModel.getValueAt(i, TestCaseDataTableColumns.STATUS.INDEX).toString());

			switch(status)
			{
			case Passed: 	passed++; break;
			case Failed: 	failed++; break;
			case Aborted: 	aborted++; break;
			case NA: 		na++; break;
			case Running: 	running++; break;
			case Pending:	pending++; break;
			case Cancelled: cancelled++; break;
			case Warning:	warning++; break;
			case Timeout:	timeout++; break;
			}
		}

		// Update Pane Data
		this.totalScript.setText(String.format("%1$3s", total));
		this.passedCount.setText(String.format("%1$3s", passed));
		this.failedCount.setText(String.format("%1$3s", failed));

		NumberFormat formater = NumberFormat.getInstance();
		formater.setMaximumFractionDigits(2);
		if(total == 0)
		{
			this.passedPercentage.setText(String.format("%1$6s", 0));
			this.failedPercentage.setText(String.format("%1$6s", 0));
		}
		else
		{
			this.passedPercentage.setText(String.format("%1$6s", formater.format(passed * 100.0/total)));
			this.failedPercentage.setText(String.format("%1$6s", formater.format(failed * 100.0/total)));
		}


		// Update pane with the scheduled testcase status
		TestManager testManager = TestManager.getInstance();
		if(testManager.isRunning())
		{
			int scheduled = testManager.getScheduledTestCaseCount();
			long completed = testManager.getCompletedCount();

			this.scheduledScript.setText(String.format("%1$3s", scheduled));
			this.completedScript.setText(String.format("%1$3s", completed));
			String scriptName = "";
			scriptName = scriptName.length() >=20? (scriptName.substring(0, 17) + "...") : scriptName;
//			this.currentScript.setText(scriptName);
//			this.currentStatus.setText(testManager.getActiveTestCase().getStatus().toString());
		}
		else
		{
			this.scheduledScript.setText(String.format("%1$3s", 0));
			this.completedScript.setText(String.format("%1$3s", 0));
			this.currentScript.setText("NA");
			this.currentStatus.setText("NA");
		}

		DefaultKeyedValues values = new DefaultKeyedValues();
		values.addValue(TestCaseStatus.NA, na);
		values.addValue(TestCaseStatus.Passed, passed);
		values.addValue(TestCaseStatus.Failed, failed);
		values.addValue(TestCaseStatus.Aborted, aborted);
		values.addValue(TestCaseStatus.Warning, warning);
		values.addValue(TestCaseStatus.Pending, pending);
		values.addValue(TestCaseStatus.Running, running);
		values.addValue(TestCaseStatus.Cancelled, cancelled);
		values.addValue(TestCaseStatus.Timeout, timeout);

		DefaultPieDataset dataSet = new DefaultPieDataset(values);
		JFreeChart pieChart = ChartFactory.createPieChart3D("Status Chart", dataSet);
		
		PiePlot3D plot = (PiePlot3D)pieChart.getPlot();
		plot.setSectionPaint(TestCaseStatus.NA, Color.GRAY);
		plot.setSectionPaint(TestCaseStatus.Passed, Color.GREEN);
		plot.setSectionPaint(TestCaseStatus.Failed, Color.RED);
		plot.setSectionPaint(TestCaseStatus.Aborted, Color.RED);
		plot.setSectionPaint(TestCaseStatus.Warning, Color.YELLOW);
		plot.setSectionPaint(TestCaseStatus.Pending, Color.CYAN);
		plot.setSectionPaint(TestCaseStatus.Running, Color.YELLOW);
		plot.setSectionPaint(TestCaseStatus.Cancelled, Color.WHITE);
		plot.setSectionPaint(TestCaseStatus.Timeout, Color.WHITE);
		
		chartsDetailPane.setChart(pieChart);
		chartsDetailPane.setPreferredSize(new Dimension(300, 275));
		chartsDetailPane.repaint();
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {
		summaryPane = new javax.swing.JPanel();
		total = new javax.swing.JLabel();
		passed = new javax.swing.JLabel();
		failed = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		totalScript = new javax.swing.JLabel();
		passedCount = new javax.swing.JLabel();
		failedCount = new javax.swing.JLabel();
		passedPercentage = new javax.swing.JLabel();
		failedPercentage = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		scheduledScript = new javax.swing.JLabel();
		completedScript = new javax.swing.JLabel();
		currentScript = new javax.swing.JLabel();
		currentStatus = new javax.swing.JLabel();
		jSeparator2 = new javax.swing.JSeparator();
		exportTestResult = new javax.swing.JButton();
		exportTestResult.addMouseListener(new ExportButtonMouseListener(this.table));
		
		DefaultPieDataset dataSet = new DefaultPieDataset();
		JFreeChart pieChart = ChartFactory.createPieChart3D("Pie Chart", dataSet);
		chartsDetailPane = new ChartPanel(pieChart);
		
		summaryPane.setBackground(new java.awt.Color(255, 255, 255));
		summaryPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Summary", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24))); // NOI18N

		total.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		total.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		total.setText("Total:");

		passed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		passed.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		passed.setText("Passed:");

		failed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		failed.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		failed.setText("Failed:");

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		jLabel1.setText("Scheduled:");

		jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		jLabel2.setText("Cmpleted:");
		jLabel2.setName(""); // NOI18N

		jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		jLabel3.setText("Current Script:");

		jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		jLabel4.setText("Current Status:");

		totalScript.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		totalScript.setText("0");

		passedCount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		passedCount.setForeground(new java.awt.Color(0, 153, 0));
		passedCount.setText("0");

		failedCount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		failedCount.setForeground(new java.awt.Color(204, 0, 0));
		failedCount.setText("0");

		passedPercentage.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		passedPercentage.setForeground(new java.awt.Color(0, 153, 0));
		passedPercentage.setText("0");

		failedPercentage.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		failedPercentage.setForeground(new java.awt.Color(204, 0, 0));
		failedPercentage.setText("0");

		jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		jLabel8.setForeground(new java.awt.Color(0, 153, 0));
		jLabel8.setText("%");

		jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		jLabel9.setForeground(new java.awt.Color(204, 0, 0));
		jLabel9.setText("%");

		scheduledScript.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		scheduledScript.setText("0");

		completedScript.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		completedScript.setText("0");

		currentScript.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		currentScript.setText("NA");

		currentStatus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		currentStatus.setText("NA");

		jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

		javax.swing.GroupLayout summaryPaneLayout = new javax.swing.GroupLayout(summaryPane);
		summaryPane.setLayout(summaryPaneLayout);
		summaryPaneLayout.setHorizontalGroup(
				summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(summaryPaneLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(summaryPaneLayout.createSequentialGroup()
										.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(summaryPaneLayout.createSequentialGroup()
														.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(total, javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(passed, javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(failed, javax.swing.GroupLayout.Alignment.TRAILING))
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(totalScript)
																		.addGroup(summaryPaneLayout.createSequentialGroup()
																				.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(passedCount)
																						.addComponent(failedCount))
																						.addGap(24, 24, 24)
																						.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addGap(18, 18, 18)
																						.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																								.addGroup(summaryPaneLayout.createSequentialGroup()
																										.addComponent(failedPercentage)
																										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(jLabel9))
																										.addGroup(summaryPaneLayout.createSequentialGroup()
																												.addComponent(passedPercentage)
																												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																												.addComponent(jLabel8))))))
																												.addGroup(summaryPaneLayout.createSequentialGroup()
																														.addComponent(jLabel1)
																														.addGap(5, 5, 5)
																														.addComponent(scheduledScript)
																														.addGap(30, 30, 30)
																														.addComponent(jLabel2)
																														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(completedScript)))
																														.addContainerGap(68, Short.MAX_VALUE))
																														.addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
																														.addGroup(summaryPaneLayout.createSequentialGroup()
																																.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																		.addComponent(jLabel3)
																																		.addComponent(jLabel4))
																																		.addGap(18, 18, 18)
																																		.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																				.addComponent(currentScript)
																																				.addComponent(currentStatus))
																																				.addGap(0, 0, Short.MAX_VALUE))))
				);
		summaryPaneLayout.setVerticalGroup(
				summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(summaryPaneLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(total)
								.addComponent(totalScript))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(summaryPaneLayout.createSequentialGroup()
												.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
														.addGroup(summaryPaneLayout.createSequentialGroup()
																.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(passed)
																		.addComponent(passedCount))
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(failed)
																				.addComponent(failedCount)))
																				.addComponent(jSeparator2))
																				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																				.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(jLabel1)
																						.addComponent(jLabel2)
																						.addComponent(scheduledScript)
																						.addComponent(completedScript))
																						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																								.addComponent(jLabel3)
																								.addComponent(currentScript))
																								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																										.addComponent(jLabel4)
																										.addComponent(currentStatus)))
																										.addGroup(summaryPaneLayout.createSequentialGroup()
																												.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																														.addComponent(passedPercentage)
																														.addComponent(jLabel8))
																														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																														.addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																																.addComponent(failedPercentage)
																																.addComponent(jLabel9))))
																																.addContainerGap(21, Short.MAX_VALUE))
				);

		javax.swing.GroupLayout chartsDetailPaneLayout = new javax.swing.GroupLayout(chartsDetailPane);
		chartsDetailPane.setLayout(chartsDetailPaneLayout);
		chartsDetailPaneLayout.setHorizontalGroup(
				chartsDetailPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE)
				);
		chartsDetailPaneLayout.setVerticalGroup(
				chartsDetailPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 275, Short.MAX_VALUE)
				);

		exportTestResult.setText("Export");

		javax.swing.GroupLayout SummaryChartPaneLayout = new javax.swing.GroupLayout(this);
		this.setLayout(SummaryChartPaneLayout);
		SummaryChartPaneLayout.setHorizontalGroup(
				SummaryChartPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(SummaryChartPaneLayout.createSequentialGroup()
						.addGroup(SummaryChartPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(summaryPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(SummaryChartPaneLayout.createSequentialGroup()
										.addContainerGap()
										.addGroup(SummaryChartPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(SummaryChartPaneLayout.createSequentialGroup()
														.addComponent(exportTestResult)
														.addGap(0, 0, Short.MAX_VALUE))
														.addComponent(chartsDetailPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
														.addContainerGap())
				);
		SummaryChartPaneLayout.setVerticalGroup(
				SummaryChartPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(SummaryChartPaneLayout.createSequentialGroup()
						.addComponent(summaryPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(chartsDetailPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
						.addComponent(exportTestResult)
						.addGap(20, 20, 20))
				);
	}

	private javax.swing.JLabel failed;
	private javax.swing.JLabel failedPercentage;
	private javax.swing.JLabel failedCount;
	private javax.swing.JLabel total;
	private javax.swing.JLabel totalScript;
	private javax.swing.JLabel scheduledScript;
	private javax.swing.JLabel passed;
	private javax.swing.JLabel passedCount;
	private javax.swing.JLabel passedPercentage;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JButton exportTestResult;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel summaryPane;
	private ChartPanel chartsDetailPane;
	private javax.swing.JLabel completedScript;
	private javax.swing.JLabel currentScript;
	private javax.swing.JLabel currentStatus;

}
