# Marvel Characters

Built using kotlin with MVVM and kotlin Coroutine , flow with clean code .

- Splash screen with blur background
- List of Characters screen with pagination using OnEndless Method
    * with loader at the end of the characters list if more characters are being fetched for the pagination.
    * click event on item to show details.
    * parallax effect to the characters’ cells when scrolling.
    * transition animation upon tapping a character and presenting its details.
    
- Search Screen with a blur background layout 
    * giving the user the ability to search for their favorite characters using nameStartsWith to allow user writing any word or character.
    * using Job to managing duplicate or unneeded requests.
    * Highlight the part of each character name that is currently typed in the search bar.
    * transition animation on tapping the magnifying glass on the previous screen and transition it to the search bar.

- Character Details Screen 
    * show image for the character in the header.
    * show image for the character background of screen with blur above it (like design).
    * show Comics, series, stories and events the character was in into separated lists
      if exist or hide the section of it if not exist.
    * load services to get the image of comics, series, stories and events.
    * create collapse toolbar for header image to give the ability stretch.
    * all items for Comics, series, stories and events is clickable to open full screen and zooming 
      using my library (leonImageView).
      
Note: some of libraries that used in this project is built by me.(LeonImageView , LeonNetworkConnectivity)