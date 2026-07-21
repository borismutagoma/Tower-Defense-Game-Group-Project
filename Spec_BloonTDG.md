# Spec Bloon TDG: Game Design Document

## Towers Overview

#### MonkeyDart Tower

*The reliable entry-level choice.*

- **Cost:** 200 Gold

- **Range:** 100

- **Fire Rate (Cooldown):** **2 Ticks** (Medium-Fast)

- **Projectile:** Dart

- **Effect:** Immediate

- **Upgrades:**
  
  - *Long Range:* +25% Range (**Cost: 100**)
  
  - *Quick Shots:* -25% Fire Rate (**Cost: 150**)
  
  - *Heavy Darts:* +1 Damage (**Cost: 250**)

---

#### Needle Tower

*Lower range but good fire rate.*

- **Cost:** 300 Gold

- **Range:** 80

- **Fire Rate (Cooldown):** **1 Ticks** (Fast)

- **Projectile:** Needle

- **Effect:** Immediate

- **Upgrades:**
  
  - *Sharpened Tips:* +20% Range (**Cost: 150**)
  
  - *Machine Gun:* -25% Fire Rate (**Cost: 200**)

---

#### SniperMonkey Tower

*Infinite range but slow reload.*

- **Cost:** 500 Gold

- **Range:** Infinite

- **Fire Rate (Cooldown):** **5 Ticks** (Slow)

- **Projectile:** Very Sharp Dart

- **Effect:** Immediate

- **Upgrades:**
  
  - *Rapid Fire:* -25% Fire Rate (**Cost: 200**)
  
  - *Caliber 50:* +2 Damage (**Cost: 300**)

---

#### Bomb Tower

*Deals area damage.*

- **Cost:** 600 Gold

- **Range:** 150

- **Fire Rate (Cooldown):** **5 Ticks** (Slow)

- **Projectile:** Bomb

- **Effect:** Damage Zone

- **Upgrades:**
  
  - *Big Blast:* +1 Damage (**Cost: 200**)
  
  - *Long Range:* +50% Range (**Cost: 250**)
  
  - *Quick Reload:* -25% Fire Rate (**Cost: 300**)
  
  - *Nuclear Bomb:* Change to **Extra Bomb** (+ Damage Zone) (**Cost: 400**)

---

#### Gorilla Tower

*Expensive but devastating in the right hands.*

- **Cost:** 300 Gold

- **Range:** 200

- **Fire Rate (Cooldown):** **4 Ticks** (Medium)

- **Projectile:** Sharp Dart

- **Effect:** Immediate

- **Upgrades:**
  
  - *Alpha Instinct:* +50% Range (**Cost: 400**)
  
  - *Smasher:* +2 Damage (**Cost: 600**)
  
  - *Primal Rage:* -70% Fire Rate (**Cost: 1000**)

---

### Support Towers

| Tower            | Cost     | Range | Speed Rate | Effect                   |
| ---------------- | -------- | ----- | ---------- | ------------------------ |
| **Slow Tower**   | 400 Gold | 100   | 10         | Slows movement speed     |
| **Freeze Tower** | 500 Gold | 100   | 15         | Freezes targets in place |

---

## Projectiles & Damages

| Projectile          | Damage Level | Special Property |
| ------------------- | ------------ | ---------------- |
| **Dart**            | 1            | N/A              |
| **Needle**          | 1            | N/A              |
| **Sharp Dart**      | 2            | N/A              |
| **Very Sharp Dart** | 4            | N/A              |
| **Bomb**            | 2            | Damage Zone : 1  |
| **Extra Bomb**      | 2            | Damage Zone :  2 |

---

## Balloons

| Balloon Type           | Life (HP) | Speed | Danger Level | Reward |
| ---------------------- | --------- | ----- | ------------ | ------ |
| **Cumulus** (Standard) | 1.0       | 1.0   | **1**        | 20 $   |
| **Zephyr** (Light)     | 0.8       | 1.5   | **2**        | 20 $   |
| **Stratus** (Heavy)    | 2.0       | 0.5   | **3**        | 40 $   |

Danger Level = $\lceil{(Life×1.5)+(Speed×0.5)−1} \rceil$

---

## Game Systems

### Economy & Player Stats

- **Starting Life:** 20 HP

- **Starting Money:** 800 Gold

- **Money Reward:** Life×20 (per balloon destroyed)

- **Life Penalty:** Losing a balloon reduces HP by the balloon's **Life** value.

- **Selling a tower:** 50% of the tower's initial price is refunded to the player.

- **Selling an upgrade:** 50% of the initial upgrade price is refunded to the player.

> [!NOTE]
> If the tower sold has purchased upgrades, 50% of the total cost is refunded to the player.

### Wave Generation & Progression

The game difficulty scales based on the current Round:

- **Total Balloons per Round:** $15 + Round + random(0,5)$

- **Difficulty Jump:** Every **3 rounds**, the Game Level increases, allowing higher-level balloons to spawn.

- **Evolution Mechanism:** After leveling up **3 times**, the lowest available balloon level no longer appears.
