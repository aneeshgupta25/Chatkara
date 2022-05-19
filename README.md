# CHATKARA (A Food Ordering App) 

This app is built using mock APIs and is similar to <b> ZOMATO </b> app. <br>
The App provides the following functionalities -> <br>
1. <b> Registering on App </b> - The user has to first register by creating an account on the app.
2. <b> Forgot Password </b> - A mail containg an OTP (valid for 24 hrs) will be sent to the user's registered email ID.
3. <b> Restaurants list </b> - User will be navigated to the Home screen where a list of restaurants is displayed. (Fetched from a mock API)
4. <b> Food Items list </b> - On selection of any restaurant, a list of food items available is displayed.
5. <b> Adding items to Cart </b> - User can select any number of dishes he likes from the list, which will be added to his cart.
6. <b> Removing items from cart </b> - After addition of items to cart, if user navigates back, the cart will be cleared. (The user will be warned for the same)
7. <b> Placing order </b> - After clicking on "Proceed to Cart", he will be directed to his cart, where the total billing amount is visible, and a confirmation message will be recieved, if the order is placed successfully.
8. <b> Favourites </b> - The user can add a restaurant to his favourites list, and can navigate to the same via navigation drawer.
9. <b> Removing from Favourites </b> - He can remove a specific restaurant from the "Favourites" list.
10. <b> Sorting the list </b> - The list displayed in the Home fragment can be sorted in 3 ways -> <br>
                               A. <b> Cost (High to Low) </b> - Expensive to Cheapest <br>
                               B. <b> Cost (Low to High) </b> - Cheapest to Expensive <br>
                               C. <b> Rating </b> - Highest rating to lowest rating <br>
10. <b> Log Out </b> - After user's confirmation, he will be logged out of the app.

## SCREENSHOTS
1. <b> Splash Screen </b> <br>
<img src="https://user-images.githubusercontent.com/77202061/169392362-31b77c16-b52e-41fa-9523-affce4c930ec.jpeg" width="200" height="400" />
2. <b> Login Screen, Registration Screen </b> with <b> Forgot Password </b>
<p float="left">
  <img src="https://user-images.githubusercontent.com/77202061/169401194-de88427a-c609-4c64-a31e-3f427d8e73e8.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169401199-37b675a8-4f27-4e9d-99a0-32457fb6f540.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169402430-42935112-5ee1-4819-bf7f-34be9fe742da.jpeg" width="200" height="400" />
</p>
3. <b> Navigation Drawer </b> 
<img src="https://user-images.githubusercontent.com/77202061/169406282-c7f7730c-e654-434a-a0d1-c2df5c4a4f2b.jpeg" width="200" height="400" />
4. <b> Restaurants List </b>
<p float="left">
  <img src="https://user-images.githubusercontent.com/77202061/169402806-e89bb0f7-3b6d-4066-8124-28c261704b2a.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169402797-ea8bc699-c994-4205-8b91-1c6cc793cc2b.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169402807-103a3af3-0977-418e-b6af-7f6ef6d6afc9.jpeg" width="200" height="400" />
</p>
5. <b> Menu List </b> for <b> "Garber Burgers" </b> <br>
   At this stage, no item is added to the cart.
<img src="https://user-images.githubusercontent.com/77202061/169403332-7cbef552-e065-40c7-bdd4-363f096cabb9.jpeg" width="200" height="400" />
6. <b> Menu List </b> for <b> "Garber Burgers" </b> <br>
   At this stage, items are added to the cart.
<img src="https://user-images.githubusercontent.com/77202061/169403624-db7230b0-c81f-49f8-8ea8-bed1fb71576b.jpeg" width="200" height="400" />
7. <b> User's Cart (with total billing amount) </b>
<img src="https://user-images.githubusercontent.com/77202061/169403999-b2ee4cd2-dd4e-4ba0-8c05-1df9df68007d.jpeg" width="200" height="400" />
8. <b> Order Confirmation Screen </b>
<img src="https://user-images.githubusercontent.com/77202061/169404257-faf37200-b361-4fc5-a9d3-96f4b30ec4d7.jpeg" width="200" height="400" />
9. <b> Order History of the User </b>
<img src="https://user-images.githubusercontent.com/77202061/169404468-424d01d0-f643-4f01-a86c-dccb9e7181d7.jpeg" width="200" height="400" />
10. <b> Adding Restaurants to Favourites, Favourites Fragment </b> and <b> Remove from Favourites</b>
<p float="left">
  <img src="https://user-images.githubusercontent.com/77202061/169404995-852352be-eee8-4294-a7a4-cf96d460951d.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169404982-b0f5be2b-5b10-42b8-93cf-3e72dc97f0a6.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169404991-9de5622a-01e2-470e-b4d5-1acc98b5beb5.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169404993-59be2a5f-0697-45cf-99e3-2d51f85428cc.jpeg" width="200" height="400" />
</p>
11. <b> Sorting Action -> Cost (low to high) </b> followed by <b> Rating </b> 
<p float="left">
  <img src="https://user-images.githubusercontent.com/77202061/169405474-e7244636-e969-48ae-8700-a7b60287d07b.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169405470-4e1ac643-bbd7-4b2c-8d02-c13e70b41b09.jpeg" width="200" height="400" />
  <img src="https://user-images.githubusercontent.com/77202061/169405476-cbd54e10-f69c-4b0a-b184-550c7c461935.jpeg" width="200" height="400" />
</p>
12. <b> FAQs (Frequently Asked Questions) </b>
<img src="https://user-images.githubusercontent.com/77202061/169406060-def4992b-21c4-46f5-916d-22dd58651d9e.jpeg" width="200" height="400" />
13. <b> Profile </b>
<img src="https://user-images.githubusercontent.com/77202061/169406609-4a1b6e10-d153-4f91-8fd3-df9501c39770.jpeg" width="200" height="400" />
14. <b> Log Out </b>
<img src="https://user-images.githubusercontent.com/77202061/169406734-a6eabaa8-1bcd-496f-a5c7-36a9fafae744.jpeg" width="200" height="400" />

# HOPE YOU LIKE THE YUMMAZING APP :)
