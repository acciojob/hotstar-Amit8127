package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User user1 = userRepository.save(user);
        int id = user1.getId();
        return id;
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
//        BASIC, PRO, ELITE,
        User user = userRepository.findById(userId).get();
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        int countOfWebSeries = 0;
        for(WebSeries webSeries : webSeriesList) {
            if(user.getAge() >= webSeries.getAgeLimit()) {
//                if(user.getSubscription().equals(SubscriptionType.BASIC)) {
//                    if(webSeries.getSubscriptionType().equals(SubscriptionType.BASIC)) {
//                        countOfWebSeries++;
//                    }
//                } else if(user.getSubscription().equals(SubscriptionType.PRO)) {
//                    if(webSeries.getSubscriptionType().equals(SubscriptionType.PRO) || webSeries.getSubscriptionType().equals(SubscriptionType.BASIC)) {
//                        countOfWebSeries++;
//                    }
//                } else if(user.getSubscription().equals(SubscriptionType.ELITE)) {
//                    if((webSeries.getSubscriptionType().equals(SubscriptionType.ELITE)
//                            || webSeries.getSubscriptionType().equals(SubscriptionType.PRO)
//                            || webSeries.getSubscriptionType().equals(SubscriptionType.BASIC))) {
                        countOfWebSeries++;
//                    }
//                }
            }
        }

        return countOfWebSeries;
    }


}
