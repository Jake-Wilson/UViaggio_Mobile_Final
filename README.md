Steve and Jake's Final Project: UViaggio

Our app, UViaggio, helps students keep track of how long they are taking to get to class. By tracking their walks to individual classes each day, a user can zero in on exactly how long that walk from the corner to Gilmer Hall takes in order to optimize their time.

--------MILESTONE CHECKUP: 11/16/2018:----------
Login and signup activity connected to Firebase for authentication and users. Includes creating a new account and signing in. (test user:
test@example.com PW: pleasework12345!). When testing user creation, please use a long and complex password like our test user does.

Main recycler view activity contains cards which will hold info about a student's classes. At the bottom of the screen, there is a FAB
that links to the AddClassActivity. AddClassActivity pulls from a web service to get class details and generates a class card for the user
back in their main recycler view activity.

Gps activity is launched from a button on the create account page for now. This activity pulls up a GMaps widget, currently pointing to Australia. In the future, the user will explode the card for their class, and the GPS map will default to the lat/lon for that specific class.

Thus, we have made progress on features including: Firebase integration, GPS, and consuming a web service. We have multiple activities, and our app is well on its way to being a wholistic, usable thing. Our next features to implement are the card explosions, editing a class, and storing trip data in sqlite.

