package de.hensel.stream;

import com.google.mu.util.stream.BiStream;

import java.util.*;

import static java.util.stream.Collectors.*;

public class Example {

    public static Collection<OutputData> conversion(Collection<InputData> input) {
        Map<String, List<InputData>> map = new HashMap<>(input.size() << 3);
        for (InputData i : input) {
            List<InputData> a = map.get(i.getName());
            if (a == null) a = new ArrayList<>(10);
            a.add(i);
            map.put(i.getName(), a);
        }
        Collection<OutputData> r = new ArrayList<>(map.size());
        for (Map.Entry<String, List<InputData>> e : map.entrySet()) {
            double sum = 0;
            for (int i = 0; i < e.getValue().size(); i++)
                sum += e.getValue().get(i).getAmount().inDefaultCurrency();
            r.add(complexeZusammenfassung(e.getKey(), sum / e.getValue().size()));
        }
        return r;
    }

    public static Collection<OutputData> conversion0(Collection<InputData> input) {
        Map<String, List<InputData>> map = new HashMap<>(input.size() << 3);
        //gruop
        for (InputData i : input) {
            List<InputData> a = map.get(i.getName());
            if (a == null) a = new ArrayList<>(10);
            a.add(i);
            map.put(i.getName(), a);
        }
        //colect results
        Collection<OutputData> r = new ArrayList<>(map.size());
        for (Map.Entry<String, List<InputData>> e : map.entrySet()) {
            //calculate avergae
            double sum = 0;
            for (int i = 0; i < e.getValue().size(); i++)
                sum += e.getValue().get(i).getAmount().inDefaultCurrency();
            r.add(complexeZusammenfassung(e.getKey(), sum / e.getValue().size()));
        }
        return r;
    }

    public static Collection<OutputData> conversion1(Collection<InputData> input) {
        Map<String, List<InputData>> groupByName = new HashMap<>(input.size() << 3);
        //grouping by name
        for (InputData personInput : input) {
            List<InputData> personData = groupByName.get(personInput.getName());
            if (personData == null) {
                personData = new ArrayList<>(10);
            }
            personData.add(personInput);
            groupByName.put(personInput.getName(), personData);
        }
        //collect results
        Collection<OutputData> result = new ArrayList<>(groupByName.size());
        for (Map.Entry<String, List<InputData>> personData : groupByName.entrySet()) {
            //calculate average
            double sum = 0;
            for (InputData person : personData.getValue()) {
                sum += person.getAmount().inDefaultCurrency();
            }
            double average = sum / personData.getValue().size();
            result.add(complexeZusammenfassung(personData.getKey(), average));
        }
        return result;
    }

    public static Collection<OutputData> conversion2(Collection<InputData> input) {
        Map<String, List<InputData>> groupByName = new HashMap<>();
        for (InputData personInput : input) {
            List<InputData> personData = groupByName.computeIfAbsent(personInput.getName(), key -> new ArrayList<>());
            personData.add(personInput);
        }
        Collection<OutputData> result = new ArrayList<>(groupByName.size());
        groupByName.forEach((personName, personData) -> {
            double average = personData.stream().mapToDouble(person -> person.getAmount().inDefaultCurrency()).average().orElseThrow();
            result.add(complexeZusammenfassung(personName, average));
        });
        return result;
    }

    public static Collection<OutputData> conversion3(Collection<InputData> input) {
        return input.stream().collect(groupingBy(InputData::getName))
                .entrySet()
                .stream()
                .map(personDataEntry ->
                        complexeZusammenfassung(personDataEntry.getKey(),
                                personDataEntry.getValue().stream()
                                        .mapToDouble(person -> person.getAmount().inDefaultCurrency())
                                        .average()
                                        .orElseThrow())
                )
                .collect(toList());
    }

    public static Collection<OutputData> conversion4(Collection<InputData> input) {
        return input.stream().collect(groupingBy(InputData::getName))
                .entrySet()
                .stream()
                .map(personDataEntry ->
                        complexeZusammenfassung(personDataEntry.getKey(), average(personDataEntry.getValue()))
                )
                .collect(toList());
    }


    public static Collection<OutputData> conversion5(Collection<InputData> input) {
        return input.stream().collect(groupingBy(InputData::getName))
                .entrySet()
                .stream()
                .map(personEntry -> new AbstractMap.SimpleEntry<>(personEntry.getKey(), average(personEntry.getValue())))
                .map(personEntry -> complexeZusammenfassung(personEntry.getKey(), personEntry.getValue()))
                .collect(toList());
    }

    public static Collection<OutputData> conversion6(Collection<InputData> input) {
        return input.stream().collect(groupingBy(InputData::getName, averagingDouble(i -> i.getAmount().inDefaultCurrency())))
                .entrySet()
                .stream()
                .map(personEntry -> complexeZusammenfassung(personEntry.getKey(), personEntry.getValue()))
                .collect(toList());
    }

    public static Collection<OutputData> conversionBiStream1(Collection<InputData> input) {
        return BiStream.from(input.stream().collect(groupingBy(InputData::getName)))
                .mapValues(i -> i.stream()
                        .map(InputData::getAmount)
                        .mapToDouble(InputData.Amount::inDefaultCurrency)
                        .average())
                .filterValues(OptionalDouble::isPresent)
                .mapValues(OptionalDouble::getAsDouble)
                .mapToObj(Example::complexeZusammenfassung)
                .collect(toList());
    }

    public static Collection<OutputData> conversionBiStream2(Collection<InputData> input) {
        return BiStream.from(input.stream().collect(groupingBy(InputData::getName)))
                .mapValues(i -> i.stream()
                        .map(InputData::getAmount)
                        .mapToDouble(InputData.Amount::inDefaultCurrency)
                        .average())
                .flatMapValues(amount -> amount.stream().boxed())
                .mapToObj(Example::complexeZusammenfassung)
                .collect(toList());
    }

    public static Collection<OutputData> conversionBiStream3(Collection<InputData> input) {
        return BiStream.from(input.stream().collect(groupingBy(InputData::getName)))
                .mapValues(Example::average)
                .mapToObj(Example::complexeZusammenfassung)
                .collect(toList());
    }

    private static OutputData complexeZusammenfassung(String name, double amount) {
        return new OutputData(name, amount);
    }

    private static double average(Collection<InputData> input) {
        return input.stream()
                .map(InputData::getAmount)
                .mapToDouble(InputData.Amount::inDefaultCurrency)
                .average()
                .orElseThrow();
    }
}
