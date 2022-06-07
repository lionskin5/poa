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
	
	
	private static String sellerPattern = "Vendedor.*";
	private static String buyerPattern = "Comprador.*";

	
	private List<Subscription> buyersSubs = new ArrayList<Subscription>();
	private List<Subscription> sellersSubs = new ArrayList<Subscription>();
	private Subscription auctioneerSub = null;
	
	public List<Subscription> getBuyersSubs() {
		return buyersSubs;
	}
	public List<Subscription> getSellersSubs() {
		return sellersSubs;
	}
	public Subscription getAuctioneerSub() {
		return auctioneerSub;
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
		
		System.out.println("Registrando fases");
		System.out.println(subscription.getMessage().getSender().getName());
		
		Pattern pattern = Pattern.compile(sellerPattern);
		Matcher match = pattern.matcher(subscription.getMessage().getSender().getName());
		
		Pattern pattern2 = Pattern.compile(buyerPattern);
		Matcher match2 = pattern2.matcher(subscription.getMessage().getSender().getName());
		
		if(match.matches()) {
			registerSeller(subscription);
		}
		else if(match2.matches()) {
			registerBuyer(subscription);
		}
		else
			registerAuctionner(subscription);
		
		return true;
	}

	
	@Override
	public boolean deregister(Subscription subscription) throws FailureException {
			
		System.out.println(subscription.getMessage().getSender().getName());
		
		Pattern pattern = Pattern.compile(sellerPattern);
		Matcher match = pattern.matcher(subscription.getMessage().getSender().getName());
		
		Pattern pattern2 = Pattern.compile(buyerPattern);
		Matcher match2 = pattern2.matcher(subscription.getMessage().getSender().getName());
		
		if(match.matches()) {
			deregisterSeller(subscription);
		}
		else if(match2.matches()) {
			deregisterBuyer(subscription);
		}
		
		else
			registerAuctionner(subscription);	
		
		return true;
	}
}
