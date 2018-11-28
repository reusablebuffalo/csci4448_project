package com.friendlyreminder;

import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.person.Contact;
import com.friendlyreminder.application.sorter.CommunicationEventSortingStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class ApplicationTests {

    @Mock
    private Contact testContact;
    private CommunicationEvent testEvent1;
    private CommunicationEvent testEvent2;
    private CommunicationEvent testEvent3;
    private CommunicationEvent testEvent4;
    private CommunicationEvent testEvent5;

    @Before
    public void setUp(){
        testContact = new Contact();
        testEvent1 =  new CommunicationEvent(new GregorianCalendar(10, 10, 10),"testNote","testType");
        testEvent2 =  new CommunicationEvent(new GregorianCalendar(10,10,11),"testNote","testType");
        testEvent3 =  new CommunicationEvent(new GregorianCalendar(10,10,12),"testNote","testType");
        testEvent4 =  new CommunicationEvent(new GregorianCalendar(11,10,12),"testNote","testType");
        testEvent5 =  new CommunicationEvent(new GregorianCalendar(11,11,12),"testNote","testType");

    }

    @Test
    public void addCommunicationEvent() {
        testContact.addCommunicationEvent(testEvent1);
        Assert.assertEquals(testContact.getCommunicationEvents().get(0),testEvent1);
    }

    @Test
    public void sortsList(){
        CommunicationEventSortingStrategy communicationEventSortingStrategy= CommunicationEventSortingStrategy.ByDateDescending;
        List<CommunicationEvent> testList = Arrays.asList(testEvent5,testEvent1,testEvent2,testEvent3,testEvent4);
        List<CommunicationEvent> expectedList = Arrays.asList(testEvent5,testEvent4,testEvent3,testEvent2,testEvent1);
        communicationEventSortingStrategy.sortList(testList);
        Assert.assertEquals(testList,expectedList);
    }

}