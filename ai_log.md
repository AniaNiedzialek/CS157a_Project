
## Prompt 1

I noticed that the date validation wasn’t perfect and wondered how I could improve it. I asked the prompt: 

```
“I am using JDBC to manage a database for a project. I want to validate if a date is given in the 
proper format. I currently use "!date.matches("\\d{4}-\\d{2}-\\d{2}")" to detect when it is invalid. 
How could I improve this to catch when the month or day is invalid, for example a month of 13?”
```
The AI responded that for a more accurate date validation, we should use the Java classes LocalDate and DateTimeFormatter, or could use a very complicated regular expression instead of the simple regular expression we currently use. I decided that we should stick with the simple validation that may let a few invalid dates through, because we still handle those cases by catching the SQLException when trying to insert the invalid date. The validation methods the AI suggested are more complex than I think the initial input validation needs to be.

## Prompt 2
Before submitting the project we were wondering about additional enhancements. The prompt we asked was 
```
“Could you propose optional improvements that could make my dance event database more efficient and 
realistic - reflecting the real-life scenarios even more accurately?” 
```
The AI suggested adding an additional table called “Registration” which would record which dancer registered for which event. With this addition, we could build a stored procedure around the new table to handle validation and rules. While the idea was useful, we decided to not implement it as adding a new table in the final phase of the project would not be consistent with previous submissions. However, we think that this would be a nice improvement for the future!
