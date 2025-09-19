

# **CoinCraft Role-Based Access & MainDashboard Documentation**

---

## **User Roles Overview**

| Role | Access Method | Description |
| ----- | ----- | ----- |
| Child | Log in using Parent/Guardian-provided Game ID and password | Primary learner using gamified modules, real-world and digital tasks |
| Parent/Guardian | Log in using their Gmail account | Registers/monitors children, verifies real-world tasks, manages coins |
| Admin | Log in using system-assigned default password | Manages content, users, coins, analytics, and system settings |

---

## **1\. Child Dashboard**

**Purpose:**  
 Provide a fun, motivational interface for children to learn, play, earn, and track progress in a financial literacy game.

**Features & Content:**

* **Welcome Message & Avatar**: Personalized greeting, avatar and accessories customization.

* **SmartCoin Balance & Earnings:**

  * Display total SmartCoins.

  * Earnings breakdown: real-world (from completed partner chores) and internal (module/performance-based coins).

* **Current Level & Progress Bar:** Animated visualization of current level and next milestone goal.

* **Active Tasks & Quests:**

  * List all in-progress digital tasks, verified real-world tasks, and upcoming system quests.

  * Each entry shows progress %, time remaining, and completion button (if applicable).

* **Badge & Achievement Showcase:**

  * Earned badge display.

  * "Coming soon" achievements with tooltips for encouragement.

* **Daily Streak Tracker:**

  * Visual calendar/streak counter showing ongoing daily engagement and streak-based bonus coins.

* **Leaderboard Snapshot:**

  * Real-time position among friends, siblings, class, or groups with motivational feedback.

* **Upcoming Events & Challenges:**

  * Timely info on seasonal quests, fundraising, or group challenges.

* **Learning Tips & Prompts:**

  * Adaptive, age-appropriate tips based on user progress and recent mistakes.

* **Task/Trade Request:**

  * When reaching certain levels, can propose new real-world tasks (assign coin value and time frame), which parent/guardian may accept/reject.

* **Messaging:**

  * Secure chat system (with moderation controls) for conversations with friends or group collaboration (task-related only).

---

## **2\. Parent/Guardian Dashboard**

**Purpose:**  
 Deliver actionable controls and insights for parent/guardian oversight, support, and interaction with their children’s learning progress.

**Features & Content:**

* **Create Child Accounts:**

  * Ability to set up login ID/password for each child to access the system.

* **Purchase Coins:**

  * Buy SmartCoins from Admin using real money (with secure payment integration), assigned to children.

* **Product Store & Permissions:**

  * Access CoinCraft product store (for additional modules or avatar items), set purchase permissions per child.

* **Child Overview:**

  * Dashboard for each linked child (or switch between children) showing coins, current level, tasks, achievements, and recent activity summary.

* **Task Verification Panel:**

  * Review all submitted real-world tasks by children pending approval.

  * Approve or reject (with comments or photo proof).

* **Progress & Analytics:**

  * Visual graphs of child’s growth: level progression, streaks, coin earning/spending, task completion ratio.

* **Badges & Milestones Earned:**

  * Overview of all badges and motivating system messages for each achievement.

* **Assign New Tasks:**

  * Interface to assign new chores, activities, or group tasks, setting deadlines and coin rewards.

  * This is capped by level-based maximum coins assignable to maintain system balance.

* **Communication Module:**

  * Message center for system announcements, reminders, or sending encouragement to children.

* **Task/Trade Request Panel:**

  * Accept/approve or reject trade/task requests submitted by children (with rationale if rejected).

* **Resource Center:**

  * Curated FAQs, guides, and system-provided financial literacy materials (some tasks require parent involvement).

  * Tips for financial education at home and maximizing the platform’s impact.

---

## **3\. Admin Dashboard**

**Purpose:**  
 Grant full administrative power for system stability, user/content management, and analytics for continual improvement.

**Features & Content:**

* **User Login:**

  * Admin authenticates with default ID and password; prompted to change on first login for security.

* **User Management:**

  * View/add/edit/deactivate children and parent/guardian accounts.

  * Assign and revoke roles, reset passwords, and audit user status.

* **Role and Access Control:**

  * Detailed controls for customizing RBAC policies.

* **Content & Quest Management:**

  * Create, edit, retire, or temporarily disable internal modules (e.g., new financial topics, badges, levels, challenges).

* **Task Monitoring & Reporting:**

  * Dashboard with full audit trail and quick filtering by user, role, status, date, and task type.

* **Analytics & Insights:**

  * Aggregate charts and reports: platform usage, engagement rates, most/least used modules, progress heatmaps, and partner activity.

* **System Settings:**

  * Configure SmartCoin rules, exchange rates (if ever made real), level progression thresholds, partner permissions, notification defaults, etc.

* **Product Store Management:**

  * Manage store items, approve parent purchases, add new shop items.

* **Support & Feedback Management:**

  * Inbox for user/system bug reports, feedback, and communication with parents/children.

* **Audit Logs:**

  * View all admin changes and significant system events for transparency and troubleshooting.

---

## **Role-Based Features Matrix**

| Feature | Child | Parent/Guardian | Admin |
| :---: | :---: | :---: | :---: |
| Game Login (ID/Pass) | ✔ | (Creates kids) | ✔ |
| Gmail Login |  | ✔ |  |
| Dashboard/Avatar Customization | ✔ | Limited (view) | ✔ |
| SmartCoin Balance | ✔ | ✔ | (all) |
| Assign/Complete Tasks | ✔ | ✔ | ✔ |
| Real-World Task Verification |  | ✔ | ✔ |
| Learning Progress Tracking | ✔ | ✔ | (all) |
| Badges/Achievements Showcase | ✔ | ✔ | ✔ |
| Daily Streak & Incentives | ✔ | ✔ (view) | ✔ |
| Leaderboard | ✔ | ✔ (view) | ✔ |
| Purchase Request/Shop Access |  | ✔ | ✔ |
| Messaging/Comms | Group | System/Child | All |
| Resource Center | Limited | ✔ | ✔ |
| Content/Quest Management |  |  | ✔ |
| Analytics & Reports | Personal | Child only | System |
| System Settings/Admin Tools |  |  | ✔ |
| Support & Feedback |  |  | ✔ |
| Audit Log |  |  | ✔ |

---

## **Implementation Notes**

* All dashboards must check user role on login and render only authorized features.

* Role-based API endpoints should follow the least privilege principle.

* UI/UX should employ clear visual cues to distinguish dashboard role and scope.

* Messaging/comms features should be sandboxed and moderated, especially for children.

* Parents’ purchase flow needs secure payment integration with receipts and admin approval.

* Admin actions require audit logging for accountability.

---

## **(A) UI Mockup / Wireframe Descriptions**

### **1. Child Dashboard Mockup**

**Top Bar:** Avatar, Welcome "Good morning, Alex!" & SmartCoin balance

**Main Area:**
- **Level Progress Bar** (with big level number and animation)
- **Active Tasks List:** Card for each task (title, type, % progress, deadline, big "Complete" button if ready)
- **Badges Strip:** Horizontally scrolling earned badges & "Coming Soon" slots
- **Leaderboard Panel:** Current rank, jump to full friends view
- **Daily Streak Widget:** Calendar with glowing days and streak counter
- **Event Banner:** Carousel of special events, challenges
- **Request Mission Button:** (enabled after N levels)

**Bottom Bar:** Navigation to Home, Tasks, Messages (badge count), Shop

### **2. Parent/Guardian Dashboard Mockup**

**Header:** Profile, child selector

**Main Area:**
- **Child Overview:** SmartCoin total, level, last activity, streak
- **Task Verification Table:** List with task info, child, time, approve/reject, comment/photo
- **Assign Task Panel:** Form to create a new task (name, description, reward, deadline, type – single/group)
- **Purchase/Shop Button:** Clearly labeled, with coin balance and "Buy More" option
- **Progress Analytics Graphs:** Time charts, badge unlock timelines, streak graphs
- **Messages Area:** Reminders, system notes, communication
- **Resource Center:** Clickable tile ("Tips", "FAQs", "Guides")

### **3. Admin Dashboard Mockup**

**Sidebar Navigation:** User Management, Content/Quest, Coins & Economy, Analytics, Shop, Settings, Logs

**Main Panel:**
- **Contextual content** (table of users, content edit forms, quests matrix, analytics charts)
- **User Management:** List, add/edit, role assign, disable, search/filter
- **Content Mgmt:** Table (title, type, state), new quest/badge creation popup
- **Pending Approvals:** Rolling panel for flagged or urgent actions
- **Analytics:** Dashboards (daily/weekly/monthly stats, quest engagement, coin economy)
- **Logs & Feedback:** List, search, filter, respond

---

## **(B) Data Flow Diagrams**

### **1. Real-World Task Creation & Approval (Parent→System→Child)**

```
[Parent Dashboard]
    |
"Assign Task" form
    |
    v
[Server/API] -- stores task --> [Child Dashboard]
    |
<Child marks "Complete">
    |
    v
[Server/API]
    |
[Parent Dashboard "Verify"]
    |
(approve/reject)
    v
[Server/API updates status]
    |
[Child sees badge, reward or feedback]
```

*Legend: Data/action flows; server coordinates updates between dashboards.*

### **2. Coin Purchase (Parent→Admin→Child)**

```
Parent -> "Buy Coins" -> Server [purchase record] -> Admin Notification Panel
    |
(Admin reviews, approves transaction)
    v
Server credits coins to Parent account (notify both parties)
    |
Parent assigns to child (if needed)
    |
Child receives updated balance
```

### **3. Authentication Flow**

**a. Child**
- System displays login form: Game ID + password
- On submit:
  - Validate credentials against secure DB
  - If success: fetch and display personalized dashboard
  - If fail: lock after N attempts, display friendly "Try again" / "Reset link (via parent)"

**b. Parent/Guardian**
- System displays "Sign in with Gmail" (OAuth2)
- On Google Auth success:
  - Check for registered parent in DB
  - If new: prompt to create child account(s)
  - If returning: show dashboard
  - Errors: invalid account, Google error, no child attached ("Contact admin/support" message)

**c. Admin**
- Username + default password login form
- On first login: force password reset
- Usual lockout and logging for failed attempts

---

## **(C) Error Handling Scenarios**

| Role | Scenario | User Feedback / Handling |
| --- | --- | --- |
| **Child** | Wrong password/ID | "Incorrect Game ID or password. Please try again." |
| | No/slow internet | "No connection. Please try later." + retry option |
| | Task submission fails (server) | "Could not submit task. Please check your network and retry." |
| | Unauthorized feature access | "This feature unlocks at a higher level!" |
| | Trade request exceeds allowed coins/time | "That request is too large for your level!" |
| **Parent** | Invalid Google sign-in | "Google sign-in failed. Please try again or use another account." |
| | Coin purchase fails (payment) | "Purchase could not be completed. No funds deducted." |
| | Child not shown (data sync error) | "We're syncing your children's records. Please refresh." |
| **Admin** | Unauthorized area | "You do not have permission to view this section." |
| | Data update fails (server) | "Update did not complete. Please try again." |
| | Conflicting content edits | "This item has been changed elsewhere. Reload to update." |
| | Excessive failed logins | "This account is temporarily locked. Contact system admin." |

**Note:** All roles should see accessible help links and support options on error screens. All serious errors should be logged and visible to admins for review.

**Tip:** These should be supported by custom UI popups, animated tooltips, and helpful inline prompts. Wireframes/mockups and automated tests (JUnit, UI tests) should cover all main flows and error states.

---

