# TestngSeDataDriven
This is a java test that reads JSON test case data into a map and runs the tests and logs the results. The libraries used are json.simple, log4j, testng and selenium, and some Java collections. 

This test was written as a learning exercise to practice with Java hashmap and data interchange, and to demonstrate data driven tests with a portable data format (JSON). I am logging the test progress and results, so the log file will show the flow of the script. Note that I am using SimpleJson library and manually populating a hashmap. I realize that GSON or Jackson libraries would likely be the easier way to do this, but those seemed a bit magical for this learning exercise. 

The tests are a simple registration validation against a QA site using selenium. The test data contains 2 failing tests and 3 successful. See the MP4 file for a quick overview of the testing. Note the video is 68MB and 1 minute long, but it downloads pretty quickly. 
