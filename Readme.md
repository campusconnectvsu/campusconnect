# Milestone 1 - Campus Connect (Unit 7)

## Table of Contents
- [Overview](#overview)
- [App Evaluation](#app-evaluation)
- [Product Spec](#product-spec)
- [Wireframes & User Flow](#wireframes)
- [Timeline]()

---

## Overview

### Description
**Campus Connect** is an Android mobile application designed **exclusively for Virginia State University (VSU) students** to discover, manage, and attend campus events in one central platform.

At VSU, event information is scattered across flyers, emails, and social media, leading to missed opportunities and low participation. Campus Connect addresses this challenge by providing a **campus-only hub** where students can:

- Browse events divided into **Cultural, Educational, Social, Career-Oriented, Sports, and Community Service** categories
- Use a **real-time interactive map** to locate events and (optionally) see friends, classmates, and faculty locations, with **privacy features** like ghost mode
- RSVP directly to events with one click (**Accept/Decline**)
- Accepted events go automatically into a **personal calendar** 
- Declined events are removed from the feed
- Receive **smart notifications** about upcoming events, time, and location

By being **limited to VSU students via .edu email verification**, the app fosters a **safe and trusted environment** that strengthens the sense of community and belonging on campus.

---

## App Evaluation

- **Category**: Campus Event Management / Student Engagement
- **Mobile**: Android app built with Kotlin (Android Studio), Firebase for authentication + notifications, Google Maps SDK for real-time maps
- **Story**: VSU students often miss out on events because information is fragmented. Campus Connect creates a **single, student-only platform** where every event is easy to find and track.
- **Market**: Targeted only for Virginia State University. This ensures exclusivity and a **community-focused adoption** compared to broader event apps.
- **Habit**: Students will use it daily to browse events, weekly to RSVP, and regularly to check reminders and maps before attending events.
- **Scope**: MVP includes login with VSU .edu verification, event feed with categories, RSVP system, calendar integration, real-time map, and notifications. Future expansions may include reviews, event feedback, and advanced social features.

---

## Product Spec

### 1. User Features

**Required Features**
- **Log In / Sign Up** with VSU .edu verification
- **Bottom Navigation Tabs**: Explore, Events, Map, Profile
- **Hamburger Menu (☰)**: My Profile, Messages, Calendar, Contact Us, Settings, Help & FAQs, Sign Out
- **Event Feed & Search** organized by categories: *Cultural, Educational, Social, Career-Oriented, Sports, Community Service*
- **Event Detail Screen** with description, RSVP choices (**Accept / Decline**)
- **RSVP Flow**:
  - Accept → Event is added directly to Calendar
  - Decline → Event is removed from the feed
- **Calendar Integration** to view RSVP’d events and reminders
- **Real-Time Map** with event locations, friends/faculty visibility (opt-in)
- **Notifications & Reminders** for upcoming events

**Optional Features**
- **User Location Sharing** (opt-in with ghost mode and block features)
- **Two-Factor Authentication** for stronger security
- **Event Reviews & Feedback** for organizers and future improvements

---

### 2. Screen Archetypes

- **Login/Sign Up Screen** – Authenticate with VSU .edu email
- **Explore (Home Feed)** – Browse events, categories, search
- **Events (Event List Page)** – Full event listing by category/date
- **Event Detail Screen** – Info + RSVP choices (Accept/Decline)
- **Map Screen** – Interactive map of events, friends, faculty (with Ghost Mode)
- **Profile Screen** – User info, RSVP history, settings shortcut
- **Calendar Screen** – RSVP’d events with automatic updates when accepted
- **Messages Screen** – Event or private messaging
- **Notifications Screen** – Reminders and updates
- **Settings & FAQs Screen** – Account, privacy, help, and support
---

### 3. Navigation

**Bottom Navigation Bar (Global)**
- **Explore** → Event discovery feed with filters
- **Events** → Full event list with RSVP choices
- **Map** → Real-time map of events, friends, faculty
- **Profile** → User account, preferences, RSVP history

**Hamburger Menu (from Explore/Home)**
- My Profile
- Messages
- Calendar
- Contact Us
- Settings
- Help & FAQs
- Sign Out

**Flow Navigation**
- Login → Explore (Home Feed)
- Explore → Event Detail → RSVP → (Accept → Calendar | Decline → Removed from feed)
- Explore → Events (list view)
- Event Detail → Map (event location + attendees)
- Calendar → Event Detail → Notifications
- Profile → Edit Interests / Privacy → Personalized Suggestions
- Settings → FAQs, Account, Security, Contact Us

---

## Wireframes

![IMG_2192 HEIC](https://github.com/user-attachments/assets/e2fd5057-6e4c-406e-b05c-6c237656d9fd)

### [BONUS] Digital Wireframes & Mockups

### Attached below is the link for figma :
https://www.figma.com/design/LXtFz8ENdOwSRn2Xtrfjr6/Campus-Connect_VSU?node-id=0-1&t=Tv5TT5ZgtnQaZiWT-1


<img width="1057" height="945" alt="Screenshot 2025-10-31 at 11 38 08 AM" src="https://github.com/user-attachments/assets/b55bae68-9d86-4153-ac18-db34557b410c" />


### [BONUS] Interactive Prototype

### RSVP Flow
- On the **Event Detail screen**, students choose:
  - **Accept** → Event is instantly added to their Calendar and a reminder is created.
  - **Decline** → Event disappears from their feed and Calendar.

---

### 1. Login / Sign Up
- Fields: Full Name, VSU Email, Password, Confirm Password
- Options: Remember Me, Forgot Password
- Verification: Code sent to `xyz@vsu.edu`
- Flow: **Login → Explore**

---

### 2. Explore (Home Feed)
- Header: VSU Logo + Search + Filters
- Categories: Sports, Social, Food, Art, Cultural, Educational, Career-Oriented, Community Service etc
- Event Cards: Title, Date/Time, RSVP buttons (Accept/Decline), Location, Attendees count
- Sidebar: Opens **hamburger menu**

---

### 3. Events (Event List Page)
- Full event list with filters (Cultural, Career, Sports, etc.)
- RSVP choices visible inline
- Quick actions: Invite, Share

---

### 4. Event Detail Screen
- Banner image + Event Name
- Description (expandable)
- Organizer details + Follow/Message
- RSVP: **Accept (→ Calendar)** | **Decline (→ Remove from feed)**
- Location info with link to **Map**

---

### 5. Map
- Interactive map with event pins and friend/faculty locations
- Toggle: Ghost Mode ON/OFF
- Tap pin → Event Detail

---

### 6. Calendar
- Auto-updated with RSVP’d events
- Daily/weekly view with reminders
- Example: “10 June – Gala Music Festival, 9:00 PM”

---

### 7. Profile
- User info: name, email, followers/following
- Tabs: About Me, Interests, RSVP History
- Privacy: Ghost Mode toggle, Manage Blocked Users

---

### 8. Messages
- Event-based temporary chats
- Direct messages with students/faculty

---

### 9. Notifications
- Push + Email notifications for RSVP’d events
- Example: “Reminder – Women’s Leadership Conference today at 4:00 PM”

---

### 10. Settings & FAQs
- Account management (Change Password, Delete Account, Logout)
- Privacy (Ghost Mode, Blocked Users)
- Contact Us (Phone: 804-524-5000, Email: xyz@vsu.edu)
- FAQs:
  - How do I create an account? Sign up with VSU email.
  - What if I forget my password? Use “Forgot Password.”
  - Can I cancel my RSVP? Yes, select Decline on event page.
  - What is Ghost Mode? Hides live location.

---
## Project Timeline
### Fall Semister 2025


| Weeks  | Date Range      | Task Description                                                                                        |
| ------ | --------------- | ------------------------------------------------------------------------------------------------------- |
| Week 1 | Nov 1- Nov 7    | Firebase setup and Data model: initialize all firebase servers and define a scheme for user and events. |
| Week 2 | Nov 8 - Nov 14  | VSU Email Auth & Profile creation :Implement login in/ sign up logic and vsu edu domain verification.   |
| Week 3 | Nov 15 - Nov 28 | Event Submission /Read: implement event creation function, event update function, and event deletion.   |
| Week 4 | Nov 22- Nov 28  |Basic Navigation UI: implement the bottom navigation tabs and hamburger menu. |
| Week 5 | Nov 29 - Dec 2  | Explore Feed UI, Category Filters: design the main event feeds layout and category filtering. |

### Sprin Semister 2026

| Weeks  | Date Range      | Task Description                                                                                        |
| ------ | --------------- | ------------------------------------------------------------------------------------------------------- |
| Week 1-2 | Jan 20- Fb 2    | Discovery and RSVP: implement final  event detail screen UI  and rsvp function. |
| Week 3-4 | Feb 3 - Feb 16  | Calendar & Utility: implement the calendar view and firebase cloud messaging for notification and messages.  |
| Week 5-7 | Feb 17 - Mar 9 | Real time Mapping:integrate Google Map SDK,real time location sharing,and ghost mode function.    |
| Week 8-11 | Mar 10 - April 7  |Testing & polishing:test all features for bugs, and finalize the app. |
| Week 12-16 | April 8 - May 12  | Finalization: Complete bug fixes, final code review, and presentation. |


# Milestone 2 - Build Sprint 1 (Unit 8)

## GitHub Project board
<img width="1710" height="1088" alt="Screenshot 2025-11-11 at 12 08 35 PM" src="https://github.com/user-attachments/assets/49d4f30d-e841-4416-9a64-e630944afcb1" />


## Issue cards
<img width="1699" height="1092" alt="Screenshot 2025-11-11 at 12 07 58 PM" src="https://github.com/user-attachments/assets/0f7b74fa-07c1-4ab7-8828-5131d99d5d1f" />

- This is for Milestone 2: Sprint 1 


## Issues worked on this sprint
List the issues you completed this sprint

- Messages

<img width="545" height="1183" alt="Screenshot 2025-11-30 165051 22" src="https://github.com/user-attachments/assets/9da77908-d925-4537-ac9d-6e2f68523c66" />

     
- SplashPage
  
<img width="500" height="1200" alt="Screenshot_20251202_171207" src="https://github.com/user-attachments/assets/dbd1e896-ac1d-4052-a8f7-0c739fb19288" />

![splashpage](https://github.com/user-attachments/assets/73f3e536-61a7-4a1e-846d-b1a3582ffdec)



# Milestone 3 - Build Sprint 2 (Unit 9)

## GitHub Project board

<img width="1710" height="1069" alt="Screenshot 2025-12-02 at 8 07 39 PM" src="https://github.com/user-attachments/assets/410d898d-f654-4fb4-a868-c143b454d809" />


## Completed user stories
- SignUp Page
  
  [https://imgur.com/XjFpzJb.gif](https://imgur.com/gallery/signup-kY9nEuG#5sKDu4d)
  ![signup](https://github.com/user-attachments/assets/0fc41809-2358-4a5b-ba4c-a722f43370ef)


- Login Page

  [https://igmur.com/aixVA6F.gif](https://imgur.com/a/aixVA6F)
  ![login](https://github.com/user-attachments/assets/21737907-ec54-4c4a-a7c1-496ffaadeda6)

  
## App Demo Video


