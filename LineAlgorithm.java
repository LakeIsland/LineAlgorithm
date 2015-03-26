import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LineAlgorithm extends JFrame{
	private BtnPanel btnPanel;
	private ResultShowingPanel resultPanel;
	
	public LineAlgorithm(){
		super("LineAlgorithm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,800);
		setBounds(0,0,820,650);
		setLayout(null);
		setVisible(true);
		
		resultPanel = new ResultShowingPanel();
		resultPanel.setSize(620,600);
		resultPanel.setBounds(0,0,620,600);
		resultPanel.setLayout(null);
		add(resultPanel);
		
		btnPanel = new BtnPanel(resultPanel);
		btnPanel.setSize(600,600);
		btnPanel.setBounds(600,0,200,600);
		add(btnPanel);

		setVisible(true);
	}
	
	public static void main(String[] args){
		new LineAlgorithm();
	}
}



class BtnPanel extends JPanel{
	
	
	
	
	class RidePanel extends JPanel{
		JLabel rideName;
		JTextField ridePop;
		JTextField rideFreq;
		JCheckBox showResult;
		int rideIndex;
		
		public void enable(){
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
		
		public RidePanel(int Index){
			rideName = new JLabel(Integer.toString(Index+1)+" Ride");
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
	
	ResultShowingPanel resultPanel;
	ArrayList<ArrayList<ArrayList<Integer>>> totalpreinfo, totalpostinfo;
	final JTextField INPUT_TOTAL_TIME = new JTextField("500",5);
	final JTextField INPUT_REPEAT_NUMBER = new JTextField("50",5);
	String [] RIDE_NUMBER_ARRAY = {"1","2","3","4","5","6","7","8"};
	int rideNumber;
	
	public BtnPanel(ResultShowingPanel r){
		resultPanel = r;
		
		add(new JLabel("     Ride   Pop     Freq   Show"));
		
		ridePanelSet = new RidePanel[8];
		//setLayout(null);
		for(int i=0; i<8; i++){
			RidePanel temp = new RidePanel(i);
			ridePanelSet[i] = temp;
			add(temp);
		}
		
		//INPUT_TOTAL_TIME.setEnabled(false);
		//text.setBounds(0,0,100,100);
		//text.setSize(100,200);
		
		add(new JLabel("        Time        Repeat   RideNum"));
		
		add(INPUT_TOTAL_TIME);
		add(INPUT_REPEAT_NUMBER);
		
		INPUT_RIDE_NUMBER = new JComboBox<String>(RIDE_NUMBER_ARRAY);
		INPUT_RIDE_NUMBER.setSelectedIndex(4);
		rideNumber = 5;
		ridePanelSet[5].disable();
		ridePanelSet[6].disable();
		ridePanelSet[7].disable();
		
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
		
		startBtn = new JButton("start");
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
							Ride ride = new Ride(Integer.parseInt(ridePanelSet[i].ridePop.getText()),Integer.parseInt(ridePanelSet[i].rideFreq.getText()));
							rideSet.add(ride);
						}
						
						AmusementPark Park = new AmusementPark(rideSet);
						for(int t=0; t<TOTAL_TIME; t++){
							Park.timeElapse();
						}
						totalpreinfo.add(Park.getPreWaitingLineResult());
						totalpostinfo.add(Park.getPostWaitingLineResult());
					}
					resultPanel.TOTAL_RIDE_NUMBER = rideNumber;
					resultPanel.preWaitingLineResult = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpreinfo);
					resultPanel.postWaitingLineResult = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpostinfo);
					
					ArrayList<ArrayList<Double>> temp = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpreinfo);
					ArrayList<ArrayList<Double>> tempo = ArrayListCalculation.changeRowToColumn(temp);
					for(ArrayList<Double> x: tempo){
						ArrayList<Double> cutx = ArrayListCalculation.removeFirstXElements(x,400);
						System.out.println(ArrayListCalculation.averageOfArrayListOfDouble(cutx));
						System.out.println(ArrayListCalculation.stdevOfArrayListOfDouble(cutx));
					}
					
					resultPanel.setTotalTime();
					resultPanel.revalidate();
					resultPanel.repaint();
				} catch(Exception exception){}
				
			}
			//resultPanel.preWaitingLineResult = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpreinfo);
			
		});
		add(startBtn);
		
		
		//add checkbox for show PreWaitingLineResult
		showPreWaitingLineResult = new JCheckBox("preLine");
		showPreWaitingLineResult.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
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
			
		});
		showPreWaitingLineResult.setSelected(true);
		add(showPreWaitingLineResult);
		
		//add checkbox for show PostWaitingLineResult
		showPostWaitingLineResult = new JCheckBox("postLine");
		showPostWaitingLineResult.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
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
			
		});
		showPostWaitingLineResult.setSelected(true);
		add(showPostWaitingLineResult);
		
	}
}

class ResultShowingPanel extends JPanel{
	ArrayList<ArrayList<Double>> preWaitingLineResult, postWaitingLineResult;
	int TOTAL_RIDE_NUMBER = 8;
	boolean showPreWaitingLineResult,showPostWaitingLineResult;
	boolean[] showNthRideResult = new boolean[TOTAL_RIDE_NUMBER];
	int totalTime;
	int repeatNumber;
	Color[] Col = {Color.BLACK,Color.BLUE,Color.RED,Color.GREEN,Color.ORANGE,Color.CYAN,Color.YELLOW,Color.MAGENTA};
	
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
					if(preWaitingLineResult.get(time-1).get(j) > Y_MAX)
						Y_MAX = preWaitingLineResult.get(time-1).get(j).intValue();
					if(postWaitingLineResult.get(time-1).get(j) > Y_MAX)
						Y_MAX = preWaitingLineResult.get(time-1).get(j).intValue();
				}
			}
		}
		int REAL_Y_MAX = (1+(Y_MAX/10))*10;
		double X_UNIT = 550.0/totalTime;
		double Y_UNIT = 560.0/((1+(Y_MAX/10))*10);
		
		g.drawLine(50, 20, 50, 580);
		g.drawLine(50, 580, 600, 580);
		for(int i=0; i<=10; i++){
			g.drawString(Integer.toString(REAL_Y_MAX-(REAL_Y_MAX/10)*i), 0, 20+56*i);
			g.drawString(Integer.toString(i*totalTime/10), 50+55*i, 590);
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
	
	private ArrayList<Ride> rideSet;
	private int time;
	private ArrayList<ArrayList<Integer>> preWaitingLineResult, postWaitingLineResult;
	private int initialPeopleNumber;
	final static int INITIAL_TIME = 2;
	
	
	public AmusementPark(ArrayList<Ride> rideSet){
		time = 0;
		this.rideSet = rideSet;
		//this.initialPeopleNumber = initialPeopleNumber;
		preWaitingLineResult = new ArrayList<ArrayList<Integer>>();
		postWaitingLineResult = new ArrayList<ArrayList<Integer>>();
	}
	
	
	int influxFunction(int t){
		//if(t==0)
			//return initialPeopleNumber;
		if(t<5)
			return 30;
		else
			return 0;
	}
	
	
	ArrayList<Double> getDistributeProportion(){
		ArrayList<Double> proportionArray = new ArrayList<Double>();
		double totalProportion = 0;
		for(int i=0; i<rideSet.size(); i++){
			if(time<INITIAL_TIME)
				totalProportion += rideSet.get(i).popularity;
			else{
				totalProportion += rideSet.get(i).getAppealIndex();
			}
		}
		
		for(int i=0; i<rideSet.size(); i++){
			if(time<INITIAL_TIME)
				proportionArray.add(rideSet.get(i).popularity/totalProportion);
			else
				proportionArray.add(rideSet.get(i).getAppealIndex()/totalProportion);
		}
		
		return proportionArray;
	}
	
	
	HashSet<Person> getInflux(){
		
		HashSet<Person> influx = new HashSet<Person>();
		
		for(Ride ride:rideSet){
			HashSet<Person> nextPostWaitingLine = new HashSet<Person>();
			for(Person p: ride.postWaitingLine){
				if(p.getPostWaitingTimeRemained()==0)
					influx.add(p);
				else{
					p.setPostWaitingTimeRemained(p.getPostWaitingTimeRemained()-1);
					nextPostWaitingLine.add(p);
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
			rideSet.get(nextRide(getDistributeProportion())).preWaitingLine.add(p);
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

class Ride{
	
	static final double ASSESS_CONSTANT = 0.5;
	
	static int postWaitingTime(int waitingTime){
		return 0;
	}
	
	double popularity;
	int processingPeopleNumberPerTime;
	
	ArrayList<Person> preWaitingLine;
	HashSet <Person> postWaitingLine;
	
	public Ride(double popularity, int processingPeopleNumberPerTime){
		this.popularity = popularity;
		this.processingPeopleNumberPerTime = processingPeopleNumberPerTime;
		
		preWaitingLine = new ArrayList<Person>();
		postWaitingLine = new HashSet <Person>();
	}
	
	public void takeRide(){
		for(int i=0; i<Math.min(preWaitingLine.size(),processingPeopleNumberPerTime); i++){
			Person p = preWaitingLine.get(0);
			p.setPostWaitingTimeRemained(postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime));
			postWaitingLine.add(p);
			preWaitingLine.remove(0);
		}
	}
	
	public double getAppealIndex(){
		//if(((preWaitingLine.size() / processingPeopleNumberPerTime) + (postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime) * ASSESS_CONSTANT))==0)
		//	return popularity;
		if(preWaitingLine.isEmpty())
			return popularity;
		return popularity/(((double)preWaitingLine.size()/ processingPeopleNumberPerTime) + (postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime) * ASSESS_CONSTANT));
	}
}

class Person{
	private int postWaitingTimeRemained;
	
	public int getPostWaitingTimeRemained(){
		return postWaitingTimeRemained;
	}
	
	public void setPostWaitingTimeRemained(int postWaitingTimeRemained){
		this.postWaitingTimeRemained = postWaitingTimeRemained;
	}
}