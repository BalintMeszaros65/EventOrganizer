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

    public Customer(UUID id, String email, String password, String firstName, String lastName, List<String> roles,
                    Set<BookedEvent> bookedEvents) {
        super(id, email, password, firstName, lastName, roles);
        this.bookedEvents = bookedEvents;
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

    private HashMap<Object, Double> createWeightedRecommendationMapFromBookedEvents() {
        HashMap<Object, Double> weightedMap = new HashMap<>();
        Set<Event> distinctEvents = bookedEvents.stream()
                .map(BookedEvent::getEvent)
                .collect(Collectors.toSet());
        for (Event event : distinctEvents) {
            weightedMap.merge(event.getPerformance(), 1.6, Double::sum);
            weightedMap.merge(event.getOrganizer(), 1.5, Double::sum);
            weightedMap.merge(event.getGenre(), 1.4, Double::sum);
            weightedMap.merge(event.getVenue(), 1.3, Double::sum);
            weightedMap.merge(event.getCity(), 1.2, Double::sum);
            weightedMap.merge(event.getCountry(), 1.1, Double::sum);
        }
        weightedMap.keySet().removeIf(Objects::isNull);
        return weightedMap;
    }

    private Object getTopRecommendationFromWeightedMap(HashMap<Object, Double> weightedMap) {
        return Collections.max(weightedMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public Object getTopRecommendation() {
        HashMap<Object, Double> weightedMap = createWeightedRecommendationMapFromBookedEvents();
        return getTopRecommendationFromWeightedMap(weightedMap);
    }

    public Object getRecommendationByClassType(Class<?> type) {
        HashMap<Object, Double> weightedMap = createWeightedRecommendationMapFromBookedEvents();
        weightedMap.keySet().removeIf(object -> !object.getClass().isInstance(type));
        return getTopRecommendationFromWeightedMap(weightedMap);
    }
}
