package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends AppUser {
    // TODO billing address later?
    @OneToMany
    Set<BookedEvent> bookedEvents;

    public Customer(String email, String password, String firstName, String lastName, List<String> roles) {
        super(null, email, password, firstName, lastName, roles, false);
    }

    public void storeBookedEvent(BookedEvent bookedEvent) {
        if (bookedEvent == null) {
            throw new IllegalArgumentException("BookedEvent can not be null.");
        }
        if (bookedEvents == null) {
            bookedEvents = new HashSet<>();
        }
        bookedEvents.add(bookedEvent);
    }

    public BigDecimal calculateAveragePricePaidForOneTicket() {
        if (bookedEvents == null) {
            return BigDecimal.ZERO;
        } else {
            int numberOfTickets = bookedEvents.stream()
                    .filter(Objects::nonNull)
                    .map(BookedEvent::getTicketCount)
                    .reduce(0, Integer::sum);
            if (numberOfTickets == 0) {
                return BigDecimal.ZERO;
            }
            BigDecimal amountPaid = bookedEvents.stream()
                    .filter(Objects::nonNull)
                    .map(BookedEvent::getAmountPayed)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return amountPaid.divide(BigDecimal.valueOf(numberOfTickets), RoundingMode.HALF_UP);
        }
    }

    private HashMap<Object, Integer> createWeightedRecommendationMapFromBookedEvents() {
        HashMap<Object, Integer> weightedMap = new HashMap<>();
        Set<Event> distinctEvents = bookedEvents.stream()
                .map(BookedEvent::getEvent)
                .collect(Collectors.toSet());
        for (Event event : distinctEvents) {
            for (RecommendationWeightable weightable : event.getRecommendationWeightables()) {
                weightedMap.merge(weightable, weightable.calculateWeight(), Integer::sum);
            }
        }
        weightedMap.keySet().removeIf(Objects::isNull);
        return weightedMap;
    }

    private Object getTopRecommendationFromWeightedMap(HashMap<Object, Integer> weightedMap) {
        return Collections.max(weightedMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public Object getTopRecommendation() {
        HashMap<Object, Integer> weightedMap = createWeightedRecommendationMapFromBookedEvents();
        return getTopRecommendationFromWeightedMap(weightedMap);
    }

    public Object getRecommendationByClassType(Class<?> type) {
        HashMap<Object, Integer> weightedMap = createWeightedRecommendationMapFromBookedEvents();
        weightedMap.keySet().removeIf(object -> !object.getClass().isInstance(type));
        return getTopRecommendationFromWeightedMap(weightedMap);
    }
}
