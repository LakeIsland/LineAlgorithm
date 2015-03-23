import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LineAlgorithm extends JFrame{
	
	class ResultShowingPanel extends JPanel{
		boolean isFinished;
		ArrayList<ArrayList<Double>> preWaitingLineResult, postWaitingLineResult;
		int[] preWaitingLineResulte;
		int totalTime;
		Color[] Col = new Color[5];
		
		public ResultShowingPanel(){
			preWaitingLineResult = new ArrayList<ArrayList<Double>>();
			postWaitingLineResult = new ArrayList<ArrayList<Double>>();
			Col[0] = Color.BLACK;
			Col[1] = Color.BLUE;
			Col[2] = Color.RED;
			Col[3] = Color.GREEN;
			Col[4] = Color.ORANGE;
		}
		
		public void paint(Graphics g){
			double X_UNIT = 800.0/totalTime;
			double Y_UNIT = 600.0/40;
			for(int time=1;time<totalTime;time++){
				for(int j=0; j<5; j++){
					g.setColor(Col[j]);
					g.drawLine((int)((time-1)*X_UNIT), 600-(int)(preWaitingLineResult.get(time-1).get(j) * Y_UNIT), (int)(time*X_UNIT), 600-(int)(preWaitingLineResult.get(time).get(j) * Y_UNIT));
					
				}
			}
		}
	}
	
	
	ResultShowingPanel resultPanel;
	
	public LineAlgorithm(){
		super("asd");
		setSize(800,600);
		setVisible(true);
		resultPanel = new ResultShowingPanel();
		totalpreinfo = new ArrayList<ArrayList<ArrayList<Integer>>>();
		totalpostinfo = new ArrayList<ArrayList<ArrayList<Integer>>>();
		getContentPane().add(resultPanel);
	}
	
	public static ArrayList<ArrayList<ArrayList<Integer>>> totalpreinfo,totalpostinfo;
	
	public static void main(String[] args){
		int time = 500;
		LineAlgorithm lm = new LineAlgorithm();
		
		for(int k=0; k<500; k++){
			ArrayList<Ride> rideSet = new ArrayList<Ride>();
			for(int i=0; i<5; i++){
				Ride ride = new Ride(i+1,1);
				rideSet.add(ride);
			}
			AmusementPark Park = new AmusementPark(rideSet);
			for(int t=0; t<time; t++){
				Park.timeElapse();
			}
			totalpreinfo.add(Park.getPreWaitingLineResult());
			totalpostinfo.add(Park.getPostWaitingLineResult());
		}
		ArrayList<ArrayList<Double>> temp = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpreinfo);
		ArrayList<ArrayList<Double>> tempo = ArrayListCalculation.changeRowToColumn(temp);
		for(ArrayList<Double> x: tempo){
			ArrayList<Double> cutx = ArrayListCalculation.removeFirstXElements(x,200);
			System.out.println(ArrayListCalculation.averageOfArrayListOfDouble(cutx));
			System.out.println(ArrayListCalculation.stdevOfArrayListOfDouble(cutx));
		}
		
		lm.resultPanel.preWaitingLineResult = temp;
		lm.resultPanel.postWaitingLineResult = ArrayListCalculation.averageOfArrayListOfArrayListArrayListInteger(totalpostinfo);
		lm.resultPanel.isFinished = true;
		lm.resultPanel.totalTime = time;
		lm.resultPanel.repaint();
	}
}

class AmusementPark{
	
	private ArrayList<Ride> rideSet;
	private int time;
	private ArrayList<ArrayList<Integer>> preWaitingLineResult, postWaitingLineResult;
	final static int INITIAL_TIME = 20;
	
	
	public AmusementPark(ArrayList<Ride> rideSet){
		time = 0;
		this.rideSet = rideSet;
		preWaitingLineResult = new ArrayList<ArrayList<Integer>>();
		postWaitingLineResult = new ArrayList<ArrayList<Integer>>();
	}
	
	
	int influxFunction(int t){
		if(t<50)
			return 3;
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
				//System.out.println(i+" "+rideSet[i].getAppealIndex());
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
		
		double rand = Math.random();
		
		for(int i=0; i<distributeProportion.size(); i++){
			rand -=distributeProportion.get(i);
			if(rand<=0)
				return i;
		}
		return -1;
	}
	
	
	void timeElapse(){

		ArrayList<Double> distributeProportion = getDistributeProportion();

		for(Ride ride:rideSet){
			ride.takeRide();
		}
		
		for(Person p:getInflux()){
			rideSet.get(nextRide(distributeProportion)).preWaitingLine.add(p);
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
		return waitingTime;
	}
	
	double popularity;
	int processingPeopleNumberPerTime; //초당 처리 속도
	
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
		if(preWaitingLine.isEmpty())
			return popularity;
		
		return popularity/((preWaitingLine.size() / processingPeopleNumberPerTime) + (postWaitingTime(preWaitingLine.size() / processingPeopleNumberPerTime) * ASSESS_CONSTANT));
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