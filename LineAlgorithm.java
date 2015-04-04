import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LineAlgorithm extends JFrame{
	private BtnPanel btnPanel;
	private ResultShowingPanel resultPanel;
	
	public LineAlgorithm(){
		super("LineAlgorithm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(820,800);
		setBounds(0,0,840,660);
		setLayout(null);
		setVisible(true);
		
		resultPanel = new ResultShowingPanel();
		resultPanel.setSize(620,600);
		resultPanel.setBounds(0,0,620,600);
		resultPanel.setLayout(null);
		add(resultPanel);
		
		btnPanel = new BtnPanel(resultPanel);
		btnPanel.setSize(600,650);
		btnPanel.setBounds(620,0,200,650);
		add(btnPanel);

		setVisible(true);
	}
	
	public static void main(String[] args){
		new LineAlgorithm();
	}
}



class BtnPanel extends JPanel{
	
	class RidePanel extends JPanel{
		JLabel rideName, rideColor;
		JTextField ridePop;
		JTextField rideFreq;
		JCheckBox showResult;
		int rideIndex;
		
		public void enable(){
			//rideColor.setEnabled(true);
			rideName.setEnabled(true);
			ridePop.setEnabled(true);
			rideFreq.setEnabled(true);
			showResult.setSelected(true);
			showResult.setEnabled(true);
		}
		
		public void disable(){
			rideName.setEnabled(false);
			ridePop.setEnabled(false);
			rideFreq.setEnabled(false);
			showResult.setSelected(false);
			showResult.setEnabled(false);
		}
		
		public RidePanel(int Index,Color color){
			
			rideName = new JLabel(Integer.toString(Index+1)+" Ride");
			rideName.setForeground(color);
			ridePop = new JTextField(Integer.toString(Index+1),3);
			rideFreq = new JTextField("1",3);
			
			rideIndex = Index;
			showResult = new JCheckBox();
			showResult.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent arg0) {
					if(showResult.isSelected()){
						resultPanel.showNthRideResult[rideIndex] = true;
						resultPanel.revalidate();
						resultPanel.repaint();
					}
					else{
						resultPanel.showNthRideResult[rideIndex] = false;
						resultPanel.revalidate();
						resultPanel.repaint();
					}
				}
			});
			showResult.setSelected(true);
			add(rideName);
			add(ridePop);
			add(rideFreq);
			add(showResult);
		}
	}
	
	
	private JButton startBtn;
	JCheckBox showPreWaitingLineResult;
	JCheckBox showPostWaitingLineResult;
	JComboBox<String> INPUT_RIDE_NUMBER;
	JCheckBox [] showset;
	RidePanel [] ridePanelSet;
	JTextField initValue;
	
	ResultShowingPanel resultPanel;
	ArrayList<ArrayList<ArrayList<Integer>>> totalpreinfo, totalpostinfo;
	
	final JTextField timeAfter;
	final JTextArea statisticsShowingArea;
	
	final String [] RIDE_NUMBER_ARRAY = {"1","2","3","4","5","6","7","8"};
	final String [] FUNCTION_TYPE_ARRAY = {"Polynomial","Power","Logarithm","Exponential"};
	int rideNumber;
	
	public BtnPanel(ResultShowingPanel r){
		resultPanel = r;
		
		// (1) add Label
		add(new JLabel("     Ride   Pop     Freq   Show"));
		
		// (2) add 8 ridePanel
		ridePanelSet = new RidePanel[8];
		for(int i=0; i<8; i++){
			RidePanel temp = new RidePanel(i,ResultShowingPanel.Col[i]);
			ridePanelSet[i] = temp;
			add(temp);
		}
		rideNumber = 5;
		ridePanelSet[5].disable();
		ridePanelSet[6].disable();
		ridePanelSet[7].disable();
		
		// (3) add Label
		add(new JLabel("   Time    Repeat    Init    RideNum"));
		
		// (4) add textfield to receive time, repeat input
		final JTextField INPUT_TOTAL_TIME = new JTextField("500",3);
		final JTextField INPUT_REPEAT_NUMBER = new JTextField("50",3);
		add(INPUT_TOTAL_TIME);
		add(INPUT_REPEAT_NUMBER);
		
		//(9)
				initValue = new JTextField("100",3);
				add(initValue);
		
		// (5) add combobox to get ride numbers
		INPUT_RIDE_NUMBER = new JComboBox<String>(RIDE_NUMBER_ARRAY);
		INPUT_RIDE_NUMBER.setSelectedIndex(4);
		INPUT_RIDE_NUMBER.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				rideNumber = INPUT_RIDE_NUMBER.getSelectedIndex()+1;
				for(int i=0; i<8; i++){
					if(i>=rideNumber)
						ridePanelSet[i].disable();
					else
						ridePanelSet[i].enable();
				}
			}
			
		});
		add(INPUT_RIDE_NUMBER);
		
		
		add(new JLabel("      Func Type      Func Info"));
		
		final JComboBox<String> functionType = new JComboBox<String>(FUNCTION_TYPE_ARRAY);
		final JTextField functionInfo = new JTextField("1,0",4);
		functionType.setSelectedIndex(0);
		
		
		
		add(functionType);
		add(functionInfo);
		
		
		// (6) add start button
		startBtn = new JButton("      Start simulation      ");
		add(startBtn);
		add(new JLabel("Show : "));
		
		// (7) add checkbox for show PreWaitingLineResult
		showPreWaitingLineResult = new JCheckBox("preLine");
		showPreWaitingLineResult.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkShowPreLine();
			}
			
		});
		showPreWaitingLineResult.setSelected(true);
		checkShowPreLine();
		add(showPreWaitingLineResult);
		
		// (8) add checkbox for show PostWaitingLineResult
		showPostWaitingLineResult = new JCheckBox("postLine");
		showPostWaitingLineResult.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkShowPostLine();
			}

		});
		showPostWaitingLineResult.setSelected(true);
		checkShowPostLine();
		add(showPostWaitingLineResult);
		
		
		add(new JLabel("Time After : "));
		
		timeAfter = new JTextField("200",3);
		add(timeAfter);
		
		final JCheckBox showAdvancedResult = new JCheckBox("Stdev");
		add(showAdvancedResult);
		
		statisticsShowingArea = new JTextArea("----RESULT----",4,14);
		JScrollPane s = new JScrollPane(statisticsShowingArea);
		add(s);
		
		
		startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try{
					totalpreinfo = new ArrayList<ArrayList<ArrayList<Integer>>>();
					totalpostinfo = new ArrayList<ArrayList<ArrayList<Integer>>>();
					
					int TOTAL_TIME = Integer.parseInt(INPUT_TOTAL_TIME.getText());
					int REPEAT_NUMBER = Integer.parseInt(INPUT_REPEAT_NUMBER.getText());
					
					for(int k=0; k<REPEAT_NUMBER; k++){
						ArrayList<Ride> rideSet = new ArrayList<Ride>();
						for(int i=0; i<rideNumber; i++){
							FunctionType f = new FunctionType(functionType.getSelectedIndex(),functionInfo.getText());
							Ride ride = new Ride(i,Integer.parseInt(ridePanelSet[i].ridePop.getText()),Integer.parseInt(ridePanelSet[i].rideFreq.getText()),f);
							rideSet.add(ride);
						}
						
						AmusementPark Park = new AmusementPark(rideSet,Integer.parseInt(initValue.getText()));
						for(int t=0; t<TOTAL_TIME; t++){
							Park.timeElapse();
						}
						totalpreinfo.add(Park.getPreWaitingLineResult());
						totalpostinfo.add(Park.getPostWaitingLineResult());
					}
					resultPanel.TOTAL_RIDE_NUMBER = rideNumber;
					resultPanel.preWaitingLineResult = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpreinfo);
					resultPanel.postWaitingLineResult = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpostinfo);
					
					showAdvancedResult.setSelected(false);
					showStatistics();
					
					resultPanel.setTotalTime();
					resultPanel.revalidate();
					resultPanel.repaint();
				} catch(Exception exception){}
				
			}
		});
		
		timeAfter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!showAdvancedResult.isSelected())
					showStatistics();
				else
					showStatisticsAdvanced();
			}

		});
		
		showAdvancedResult.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!showAdvancedResult.isSelected())
					showStatistics();
				else
					showStatisticsAdvanced();
			}

		
			
		});
		
	}
	
	void checkShowPreLine(){
		if(showPreWaitingLineResult.isSelected()){
			resultPanel.showPreWaitingLineResult = true;
			resultPanel.revalidate();
			resultPanel.repaint();
		}
		else{
			resultPanel.showPreWaitingLineResult = false;
			resultPanel.revalidate();
			resultPanel.repaint();
		}
	}
	
	void checkShowPostLine(){
		if(showPostWaitingLineResult.isSelected()){
			resultPanel.showPostWaitingLineResult = true;
			resultPanel.revalidate();
			resultPanel.repaint();
		}
		else{
			resultPanel.showPostWaitingLineResult = false;
			resultPanel.revalidate();
			resultPanel.repaint();
		}
	}
	
	
	
	void showStatistics(){
		final StringBuilder resultStringBuilder = new StringBuilder();
		ArrayList<ArrayList<Double>> preWaitingResultReSorted = ArrayListCalculation.changeRowToColumn(resultPanel.preWaitingLineResult);
		ArrayList<ArrayList<Double>> postWaitingResultReSorted = ArrayListCalculation.changeRowToColumn(resultPanel.postWaitingLineResult);
		
		for(int i=0; i<preWaitingResultReSorted.size(); i++){
			ArrayList<Double> eachPreWaitingLineResultTrimmed = ArrayListCalculation.removeFirstXElements(preWaitingResultReSorted.get(i),Integer.parseInt(timeAfter.getText()));
			ArrayList<Double> eachPostWaitingLineResultTrimmed = ArrayListCalculation.removeFirstXElements(postWaitingResultReSorted.get(i),Integer.parseInt(timeAfter.getText()));
			resultStringBuilder.append(String.format("%.02f",ArrayListCalculation.averageOfArrayListOfDouble(eachPreWaitingLineResultTrimmed)));
			resultStringBuilder.append("\t");
			resultStringBuilder.append(String.format("%.02f",ArrayListCalculation.averageOfArrayListOfDouble(eachPostWaitingLineResultTrimmed)));
			if(i!=preWaitingResultReSorted.size()-1)
				resultStringBuilder.append("\n");
		}
		
		statisticsShowingArea.setText(resultStringBuilder.toString());
	}
	
	void showStatisticsAdvanced(){
		final StringBuilder resultStringBuilder = new StringBuilder();
		ArrayList<ArrayList<Double>> preWaitingResultReSorted = ArrayListCalculation.changeRowToColumn(resultPanel.preWaitingLineResult);
		ArrayList<ArrayList<Double>> postWaitingResultReSorted = ArrayListCalculation.changeRowToColumn(resultPanel.postWaitingLineResult);
		
		for(int i=0; i<preWaitingResultReSorted.size(); i++){
			ArrayList<Double> eachPreWaitingLineResultTrimmed = ArrayListCalculation.removeFirstXElements(preWaitingResultReSorted.get(i),Integer.parseInt(timeAfter.getText()));
			resultStringBuilder.append(String.format("%.02f",ArrayListCalculation.averageOfArrayListOfDouble(eachPreWaitingLineResultTrimmed)));
			resultStringBuilder.append("\t");
			resultStringBuilder.append(String.format("%.04f",ArrayListCalculation.stdevOfArrayListOfDouble(eachPreWaitingLineResultTrimmed)));
			resultStringBuilder.append("\n");
		}
		
		resultStringBuilder.append(" - - - - - - - - - - - - - - -  - - - \n");
		
		for(int i=0; i<preWaitingResultReSorted.size(); i++){
			
			ArrayList<Double> eachPostWaitingLineResultTrimmed = ArrayListCalculation.removeFirstXElements(postWaitingResultReSorted.get(i),Integer.parseInt(timeAfter.getText()));
			
			resultStringBuilder.append(String.format("%.02f",ArrayListCalculation.averageOfArrayListOfDouble(eachPostWaitingLineResultTrimmed)));
			resultStringBuilder.append("\t");
			resultStringBuilder.append(String.format("%.04f",ArrayListCalculation.stdevOfArrayListOfDouble(eachPostWaitingLineResultTrimmed)));
			if(i!=preWaitingResultReSorted.size()-1)
				resultStringBuilder.append("\n");
		}
		
		statisticsShowingArea.setText(resultStringBuilder.toString());
	}
	
}

class ResultShowingPanel extends JPanel{
	
	ArrayList<ArrayList<Double>> preWaitingLineResult, postWaitingLineResult;
	int TOTAL_RIDE_NUMBER = 8;
	boolean showPreWaitingLineResult,showPostWaitingLineResult;
	boolean[] showNthRideResult = new boolean[TOTAL_RIDE_NUMBER];
	int totalTime;
	int repeatNumber;
	static final Color[] Col = {Color.BLACK,Color.BLUE,Color.RED,Color.ORANGE,Color.MAGENTA,Color.CYAN,Color.YELLOW,Color.GREEN};
	
	public ResultShowingPanel(){
		preWaitingLineResult = new ArrayList<ArrayList<Double>>();
		postWaitingLineResult = new ArrayList<ArrayList<Double>>();
	}
	
	public void setRideNumber(){
		if(!preWaitingLineResult.isEmpty()){
			TOTAL_RIDE_NUMBER = preWaitingLineResult.get(0).size();
		}
	}
	
	public void setTotalTime(){
		if(!preWaitingLineResult.isEmpty()){
			totalTime = preWaitingLineResult.size();
		}
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g.clearRect(0, 0, 800, 600);
		
		int Y_MAX = 5;
		for(int time = 1; time <totalTime; time++){
			for(int j=0; j<TOTAL_RIDE_NUMBER; j++){
				if(showNthRideResult[j]){
					if(showPreWaitingLineResult)
						if(preWaitingLineResult.get(time-1).get(j) > Y_MAX)
							Y_MAX = preWaitingLineResult.get(time-1).get(j).intValue();
					if(showPostWaitingLineResult)
						if(postWaitingLineResult.get(time-1).get(j) > Y_MAX)
							Y_MAX = postWaitingLineResult.get(time-1).get(j).intValue();
				}
			}
		}
		int REAL_Y_MAX = (1+(Y_MAX/10))*10;
		double X_UNIT = 550.0/totalTime;
		double Y_UNIT = 560.0/((1+(Y_MAX/10))*10);
		
		g.drawLine(50, 20, 50, 580);
		g.drawLine(50, 580, 600, 580);
		for(int i=0; i<=10; i++){
			g.drawString(Integer.toString(REAL_Y_MAX-(REAL_Y_MAX/10)*i), 30, 25+56*i);
			g.drawString(Integer.toString(i*totalTime/10), 45+55*i, 595);
		}
		
		
		for(int time=1;time<totalTime;time++){
			if(showPreWaitingLineResult){
				g2d.setStroke(new BasicStroke(1));
				for(int j=0; j<TOTAL_RIDE_NUMBER; j++){
					g.setColor(Col[j]);
					if(showNthRideResult[j])
						g.drawLine(50+(int)((time-1)*X_UNIT), 580-(int)(preWaitingLineResult.get(time-1).get(j) * Y_UNIT), 50+(int)(time*X_UNIT), 580-(int)(preWaitingLineResult.get(time).get(j) * Y_UNIT));
				}
			}
			
			if(showPostWaitingLineResult){
				
				g2d.setStroke(new BasicStroke(2));
				for(int j=0; j<TOTAL_RIDE_NUMBER; j++){
					g.setColor(Col[j]);
					if(showNthRideResult[j])
						g.drawLine(50+(int)((time-1)*X_UNIT), 580-(int)(postWaitingLineResult.get(time-1).get(j) * Y_UNIT), 50+(int)(time*X_UNIT), 580-(int)(postWaitingLineResult.get(time).get(j) * Y_UNIT));
				}
			}
		}
	}
}

class AmusementPark{
	private int totalTime;
	private ArrayList<Ride> rideSet;
	static int rideNumber;
	private int time;
	private ArrayList<ArrayList<Integer>> preWaitingLineResult, postWaitingLineResult;
	private int initialPeopleNumber;
	final static int INITIAL_TIME = 2;
	
	static int totalSatisfaction;
	
	public AmusementPark(ArrayList<Ride> rideSet, int initialPeopleNumber){
		time = 0;
		this.rideSet = rideSet;
		totalSatisfaction = 0;
		for(Ride r: rideSet)
			totalSatisfaction +=r.popularity;
		
		rideNumber = rideSet.size();
		this.initialPeopleNumber = initialPeopleNumber;
		preWaitingLineResult = new ArrayList<ArrayList<Integer>>();
		postWaitingLineResult = new ArrayList<ArrayList<Integer>>();
	}
	
	
	int influxFunction(int t){
		if(t==0)
			return initialPeopleNumber;
		else if(t<50)
			return 3;
		return 0;
	}
	
	
	ArrayList<Double> getDistributeProportion(Person p){
		ArrayList<Double> proportionArray = new ArrayList<Double>();
		double totalProportion = 0;
		for(int i=0; i<rideSet.size(); i++){
			if(time<INITIAL_TIME)
				totalProportion += rideSet.get(i).popularity;
			else{
				totalProportion += rideSet.get(i).getAppealIndex(p);
			}
		}
		
		for(int i=0; i<rideSet.size(); i++){
			if(time<INITIAL_TIME)
				proportionArray.add(rideSet.get(i).popularity/totalProportion);
			else
				proportionArray.add(rideSet.get(i).getAppealIndex(p)/totalProportion);
		}
		
		return proportionArray;
	}
	
	
	HashSet<Person> getInflux(){
		
		HashSet<Person> influx = new HashSet<Person>();
		
		for(Ride ride:rideSet){
			HashSet<Person> nextPostWaitingLine = new HashSet<Person>();
			for(Person p: ride.postWaitingLine){
				boolean leave = p.wantToLeave2();
				if(p.getPostWaitingTimeRemained()==0 && !leave)
					influx.add(p);
				else{
					if(!leave){
						p.setPostWaitingTimeRemained(p.getPostWaitingTimeRemained()-1);
						nextPostWaitingLine.add(p);
					}	
				}
			}
			ride.postWaitingLine = nextPostWaitingLine;
		}
		
		for(int i=0; i<influxFunction(time); i++){
			Person p = new Person();
			influx.add(p);
		}
		
		return influx;
	}
	
	
	int nextRide(ArrayList<Double> distributeProportion){
		Random random = new Random();
		
		double rand = random.nextDouble();
		
		for(int i=0; i<distributeProportion.size(); i++){
			rand -=distributeProportion.get(i);
			if(rand<=0)
				return i;
		}
		return -1;
	}
	
	void timeElapse(){

		//ArrayList<Double> distributeProportion = getDistributeProportion();

		for(Ride ride:rideSet){
			ride.takeRide();
		}
		
		for(Person p:getInflux()){
			rideSet.get(nextRide(getDistributeProportion(p))).preWaitingLine.add(p);
		}
		
		ArrayList<Integer> preResult = new ArrayList<Integer>();
		ArrayList<Integer> postResult = new ArrayList<Integer>();
		
		for(int i=0; i<rideSet.size(); i++){		
			preResult.add(rideSet.get(i).preWaitingLine.size());
			postResult.add(rideSet.get(i).postWaitingLine.size());
		}
		
		preWaitingLineResult.add(preResult);
		postWaitingLineResult.add(postResult);

		time+=1;
	}
	
	ArrayList<ArrayList<Integer>> getPreWaitingLineResult(){
		return preWaitingLineResult;
	}
	
	ArrayList<ArrayList<Integer>> getPostWaitingLineResult(){
		return postWaitingLineResult;
	}
	
}

class FunctionType{
	private int functionType;
	
	final static int POLINOMIAL = 0;
	final static int POWER = 1;
	final static int LOGARITHM = 2;
	final static int EXPONENTIAL = 3;
	
	double[] polynomialCoefficients;
	
	double power;
	
	double coefficient;
	
	public FunctionType(int type, String info){
		functionType = type;
		switch(functionType){
		case FunctionType.POLINOMIAL:
			String[] coefficients = info.split(",");
			polynomialCoefficients = new double[coefficients.length];
			
			for(int i=0; i<coefficients.length; i++){
				try{
					polynomialCoefficients[i] = Double.parseDouble(coefficients[i]);
				}catch(Exception e){System.out.println(e.getMessage());}
			}
			break;
		case FunctionType.POWER:
			try{
				String[] powerInfo = info.split(",");
				coefficient = Double.parseDouble(powerInfo[0]);
				power = Double.parseDouble(powerInfo[1]);
			}catch(Exception e){System.out.println(e.getMessage());}
			break;
		default:
			try{
				coefficient = Double.parseDouble(info);
			} catch(Exception e){System.out.println(e.getMessage());}
		}
	}
	
	int polynomialType(int waitingTime){
		double result = 0;
		int degree = polynomialCoefficients.length;
		for(int power=0; power<degree; power++){
			result += polynomialCoefficients[degree-1-power] * Math.pow(waitingTime, power);
		}
		return (int)result;
	}
	
	
	int powerType(int waitingTime){
		return (int)(Math.pow(waitingTime, power)*coefficient);
	}
	
	int logarithmType(int waitingTime){
		return (int)(Math.log(waitingTime)/Math.log(coefficient));
	}
	
	int exponentialType(int waitingTime){
		return (int)Math.exp(Math.log(coefficient)*waitingTime);
	}
	
	int postWaitingTime(int waitingTime){
		switch(functionType){
		case FunctionType.POLINOMIAL:
			return polynomialType(waitingTime);
		case FunctionType.POWER:
			return powerType(waitingTime);
		case FunctionType.LOGARITHM:
			return logarithmType(waitingTime);
		case FunctionType.EXPONENTIAL:
			return exponentialType(waitingTime);
		default:
			return 0;
		}
	}
}

/*interface Nameable{
	String name = null;
	public void setName(String name);
	public String getName();
}*/

class Ride{
	static final double ASSESS_CONSTANT = 0.5;
	private int index;
	FunctionType postWaitingFunction;
	
	double popularity;
	int processingPeopleNumberPerTime;
	
	ArrayList<Person> preWaitingLine;
	HashSet <Person> postWaitingLine;
	
	public Ride(int index, double popularity, int processingPeopleNumberPerTime, FunctionType postWaitingFunction){
		this.index = index;
		this.popularity = popularity;
		this.processingPeopleNumberPerTime = processingPeopleNumberPerTime;
		this.postWaitingFunction = postWaitingFunction;
		
		preWaitingLine = new ArrayList<Person>();
		postWaitingLine = new HashSet <Person>();
	}
	
	public void takeRide(){
		for(int i=0; i<Math.min(preWaitingLine.size(),processingPeopleNumberPerTime); i++){
			Person p = preWaitingLine.get(0);
			p.setPostWaitingTimeRemained(postWaitingFunction.postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime));
			p.takenRides[index] = true;
			p.satisfaction+=this.popularity;
			postWaitingLine.add(p);
			preWaitingLine.remove(0);
		}
	}
	
	public double getAppealIndex(Person p){
		if(p.takenRides[index])
			return 0;
		//if(((preWaitingLine.size() / processingPeopleNumberPerTime) + (postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime) * ASSESS_CONSTANT))==0)
		//	return popularity;
		if(preWaitingLine.isEmpty())
			return popularity;
		return popularity/(((double)preWaitingLine.size()/ processingPeopleNumberPerTime) + (postWaitingFunction.postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime) * ASSESS_CONSTANT));
	}
}

class Person{
	int totalTimeSpent;
	int satisfaction;
	boolean[] takenRides;
	private int postWaitingTimeRemained;

	public Person(){
		totalTimeSpent=0;
		satisfaction=0;
		takenRides = new boolean[AmusementPark.rideNumber];
	}
	
	public int getPostWaitingTimeRemained(){
		return postWaitingTimeRemained;
	}
	
	public void setPostWaitingTimeRemained(int postWaitingTimeRemained){
		this.postWaitingTimeRemained = postWaitingTimeRemained;
	}
	
	public boolean wantToLeave2(){
		//System.out.println(AmusementPark.totalSatisfaction);
		if((Math.random()+1)/2<((double)satisfaction/AmusementPark.totalSatisfaction)){
			//System.out.println(String.format("%.02f", ((double)satisfaction/AmusementPark.totalSatisfaction)));
			return true;
		}
		return false;
	}
	
	public boolean wantToLeave3(){
		for(int i=0; i<AmusementPark.rideNumber; i++){
			if(takenRides[i]==false)
				return false;
		}
		return true;
	}
}