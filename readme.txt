1.Zasady gry:
    Na początku gry każdy gracz otrzymuje pulę żetonów oraz 5 kart. W czasie gry ma on zawsze do wybory trzy ruchy:
fold - odłożenie kart
check - głosowanie o sprawdzenie kart
raise - podniesienie stawki o minimalną pulę lub wyższą

Po podniesieniu stawki przez któregoś z graczy, do wyboru są kolejne trzy ruchy:
fold - odłożenie kart
call - położenie na stól minimalną pulę
raise - podniesienie stawki o pulę wyższą niż minimalna

Gdy wszyscy graczy zagłosują o sprawdzenie kart, sprawdzany jest wynik rundy.
Dodatakowo, gdy wszyscy gracze oprócz jedngo odłożą karty, wygrywa ten który ich nie odłożył.

** Nie zdązyłem zaimplementować wymiany kart :(




2. Sposób uruchomienia programu
mvn clean compile assembly:single
java -cp poker-core/target/poker-core-1.0-SNAPSHOT.jar RunServer <number_of_player> [<port_number>]               <-- komenda do uruchomienia servera z argumentem wskazującym na liczbę graczy i opcją wybrania portu
java -cp poker-core/target/poker-core-1.0-SNAPSHOT.jar RunClient [<port_number>]         <-- komenda do uruchomienia klienta z możliwościa opcją wybrania portu

np: java -cp poker-core/target/poker-core-1.0-SNAPSHOT.jar RunServer 2 8001    <-- uruchomi serwer na porcie 8001 z liczbą graczy 2
java -cp poker-core/target/poker-core-1.0-SNAPSHOT.jar RunClient        <-- uruchomi serwer na defaultowym porcie

Aby wygenerować pokrycie testami jednostkowymi:
mvn clean install
firefox ./testing/target/site/jacoco-aggregate/index.html              <-- otwarcie raportu pokrycia jednostkowego projektu w przeglądarce firefox

**Nie udało mi się wygenerować raportu bezpośrednio w Sonarqub-ie. Raport generowałem za pomocą jacoco, a następnie importowałem go do sonarqube.
Aby podejrzeć raport w sonarqub-ie uruchamiałem komendy:
mvn clean install
mvn sonar:sonar -Dsonar.projectKey=CardDraw -Dsonar.host.url=http://localhost:9000 -Dsonar.login=e3077a1aa7704a7870a8018a9e44400e79c1ad42



*** Gdyby coś nie działało ***
1.mvn clean install
2.mvn compile assembly:single
3.spróbować uruchomić serwer i klientów


3.