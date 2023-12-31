package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        int price = 0;
        int screens = subscriptionEntryDto.getNoOfScreensRequired();

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setStartSubscriptionDate(new Date());
        if(subscriptionEntryDto.getSubscriptionType().toString().equals("BASIC")){
            price = 500 + (200 * screens);
        } else if (subscriptionEntryDto.getSubscriptionType().toString().equals("PRO")) {
            price = 800 + (250 * screens);
        }else {
            price = 1000 + (350 * screens);
        }

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);
        subscription.setTotalAmountPaid(price);
        subscription.setNoOfScreensSubscribed(screens);
        user.setSubscription(subscription);

        userRepository.save(user);

        return price;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user = userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }

        Subscription subscription = user.getSubscription();
        Integer price =subscription.getTotalAmountPaid();
        Integer currentPrice = 0;
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            // upgrade Basic to Pro
            subscription.setSubscriptionType(SubscriptionType.PRO);
            currentPrice = (price + 300) + (50 * subscription.getNoOfScreensSubscribed());
        }else {
            // upgrade pro to Elite
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            currentPrice = (price + 200) + (100 * subscription.getNoOfScreensSubscribed());
        }

        subscription.setTotalAmountPaid(currentPrice);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);

        return currentPrice - price;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int totalRevenue = 0;
        for(Subscription subscription : subscriptionList) {
            totalRevenue += subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
