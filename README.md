Testerna körs mot en lokal postgreSQL för main är det bank_db medans vid testfallen kör jag bank_db_test
Bägge använder samma användarnamn och lösenord som är postgres och chasacademy.



ag har skapat tabellerna `account` och `transaction_log` enligt uppgiften.  
Databasen sätts upp genom att köra SQL-scriptet från scratch i en tom PostgreSQL-databas,  
vilket skapar tabeller, constraints och relationer.

Grunddata för testfallen skapas inte manuellt utan sätts upp i testerna.  
Inför varje test rensas databasen och nya konton med angivna startsaldon  
(t.ex. 1000 och 500) läggs in.

Testfallen körs via Maven med kommandot `mvn test`  
och verifierar både saldoändringar och att rätt rader skapas i `transaction_log`  
vid lyckade och misslyckade överföringar.




Vid valideringsfel, till exempel ett negativt belopp, väljer jag att inte skapa någon rad i transaction_log.
Detta beror på att jag inte ser det som en faktisk transaktion, utan som ett ogiltigt anrop som stoppas direkt innan någon affärslogik körs eller någon databaspåverkan sker.
Eftersom inga saldon ändras och ingen överföring påbörjas finns det inget faktiskt affärsförlopp att logga.
transaction_log används därför endast för verkliga överföringsförsök, både lyckade och misslyckade, men inte för rena indata- eller valideringsfel.
det samma gäller omm man försöker göra transaction till samma konto.



Jag valde att använda Maven som byggverktyg eftersom det är mycket vanligt förekommande  
i Java och Spring-projekt och gör det enkelt att köra tester och bygga projektet  
på ett standardiserat sätt.

Jag har använt enums för vissa värden för att begränsa tillåtna alternativ  
och undvika fel som kan uppstå med fria strängar, vilket gör koden säkrare  
och enklare att validera.

Jag har även valt att använda flera olika exceptions för att felmeddelanden  
och felhantering ska bli så tydliga som möjligt beroende på vilken typ av fel  
som uppstår, till exempel valideringsfel, affärsfel eller tekniska fel.

Koden är uppdelad så att varje klass har ett tydligt ansvar.  
Repositories hanterar endast databasåtkomst och services innehåller affärslogik.  
Till exempel finns separata repositories för konton och transaktionsloggar,  
samt separata serviceklasser för överföringar och loggning.  
Detta gör koden mer lättläst, lättare att testa och enklare att vidareutveckla.

I transfer-metoden valideras först indata, såsom belopp och att from och to 
konto  
inte är samma. Därefter kontrolleras att båda kontona faktiskt finns i databasen,  
annars kastas ett AccountNotFoundException.

Själva överföringslogiken ligger inuti ett try-block för att kunna fånga upp fel  
som kan uppstå efter att affärslogiken har påbörjats. Innan några saldon ändras  
kontrolleras att det finns tillräckligt med medel på kontot, annars kastas ett  
InsufficientFundsException. Först efter dessa kontroller uppdateras saldon.

En TechnicalFailureException används för att simulera ett tekniskt fel mitt i  
transaktionen för att verifiera att rollback och loggning fungerar korrekt.

Vid en lyckad överföring loggas transaktionen som SUCCESS via TransactionLogService.  
Vid fel loggas transaktionen som FAILED i en separat transaktion. Detta görs genom  
att logFailed-metoden har en egen @Transactional konfiguration (som skapas via en separat spring bean), vilket säkerställer  
att loggen sparas även om huvudtransaktionen rullas tillbaka. 

Efter att felet har loggats kastas exceptionet vidare för att anropare och tester  
ska få korrekt information om varför överföringen misslyckades.