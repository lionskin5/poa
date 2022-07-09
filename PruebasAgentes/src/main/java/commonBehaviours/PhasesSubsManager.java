package commonBehaviours;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class PhasesSubsManager implements SubscriptionManager {
	
	private static String directorPattern = "Director.*";
	private static String sellerPattern = "Vendedor.*";
	private static String buyerPattern = "Comprador.*";
	
	private List<Subscription> directorSubs = new ArrayList<Subscription>(); // Si hubiera varios directores es necesaria una lista
	private List<Subscription> buyersSubs = new ArrayList<Subscription>();
	private List<Subscription> sellersSubs = new ArrayList<Subscription>();
	private Subscription auctioneerSub = null;
	
	public List<Subscription> getDirectorSubs() {
		return directorSubs;
	}
	public List<Subscription> getBuyersSubs() {
		return buyersSubs;
	}
	public List<Subscription> getSellersSubs() {
		return sellersSubs;
	}

	public Subscription getAuctioneerSub() {
		return auctioneerSub;
	}
	
	public void registerDirector(Subscription sub) {
		synchronized (directorSubs) {
			directorSubs.add(sub);
		}		
	}
	public void registerSeller(Subscription sub) {
		synchronized (sellersSubs) {
			sellersSubs.add(sub);
		}		
	}
	public void registerBuyer(Subscription sub) {
		synchronized (buyersSubs) {
			buyersSubs.add(sub);
		}		
	}
	public void registerAuctionner(Subscription sub) {
		auctioneerSub = sub;
	}
	public void deregisterDirector(Subscription sub) {
		synchronized (directorSubs) {
			directorSubs.remove(sub);
		}		
	}
	public void deregisterSeller(Subscription sub) {
		synchronized (sellersSubs) {
			sellersSubs.remove(sub);
		}		
	}
	public void deregisterBuyer(Subscription sub) {
		synchronized (buyersSubs) {
			buyersSubs.remove(sub);
		}		
	}
	public void deregisterAuctionner() {
		auctioneerSub = null;
	}
	
	
	@Override
	public boolean register(Subscription subscription) throws RefuseException, NotUnderstoodException {
		
		System.out.println(subscription.getMessage().getSender().getName());
		
		String sender = subscription.getMessage().getSender().getName();
		
		Pattern patternD = Pattern.compile(directorPattern);
		Matcher matchD = patternD.matcher(sender);
		
		Pattern patternS = Pattern.compile(sellerPattern);
		Matcher matchS = patternS.matcher(sender);
		
		Pattern patternB = Pattern.compile(buyerPattern);
		Matcher matchB = patternB.matcher(sender);
		
		
		if(matchD.matches()) {
			registerDirector(subscription);
		}
		else if(matchS.matches()) {
			registerSeller(subscription);
		}
		else if(matchB.matches()) {
			registerBuyer(subscription);
		}
		else
			registerAuctionner(subscription);
		
		return true;
	}
	
	@Override
	public boolean deregister(Subscription subscription) throws FailureException {
			
		System.out.println(subscription.getMessage().getSender().getName());
		
		String sender = subscription.getMessage().getSender().getName();
		
		Pattern patternD = Pattern.compile(directorPattern);
		Matcher matchD = patternD.matcher(sender);
		
		Pattern patternS = Pattern.compile(sellerPattern);
		Matcher matchS = patternS.matcher(sender);
		
		Pattern patternB = Pattern.compile(buyerPattern);
		Matcher matchB = patternB.matcher(sender);
		
		if(matchD.matches()) {
			deregisterDirector(subscription);
		}
		else if(matchS.matches()) {
			deregisterSeller(subscription);
		}
		else if(matchB.matches()) {
			deregisterBuyer(subscription);
		}
		else
			registerAuctionner(subscription);	
		
		return true;
	}
	
}
