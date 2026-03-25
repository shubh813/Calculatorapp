# 🚀 BUILD TRACEABILITY SCRIPT (Production Ready)

import sys
import os
import hashlib
import zipfile
import subprocess
from datetime import datetime, timedelta

# ── Colors ──────────────────────────────────────────────────────
GREEN  = "\033[92m"
RED    = "\033[91m"
YELLOW = "\033[93m"
BLUE   = "\033[94m"
BOLD   = "\033[1m"
RESET  = "\033[0m"

def print_header():
    print(f"\n{BOLD}{'='*60}{RESET}")
    print(f"{BOLD} BUILD TRACEABILITY SCRIPT{RESET}")
    print(f"{BOLD}{'='*60}{RESET}\n")

def print_section(title):
    print(f"\n{BLUE}{BOLD}── {title} ──{RESET}")

def print_ok(msg): print(f"{GREEN}  ✅ {msg}{RESET}")
def print_fail(msg): print(f"{RED}  ❌ {msg}{RESET}")
def print_info(msg): print(f"{YELLOW}  ℹ  {msg}{RESET}")

# ────────────────────────────────────────────────────────────────
def check_war_file(war_path):
    print_section("STEP 1: WAR File Check")

    if not os.path.exists(war_path):
        print_fail(f"WAR file not found: {war_path}")
        sys.exit(1)

    size = os.path.getsize(war_path)/(1024*1024)
    mod_time = datetime.fromtimestamp(os.path.getmtime(war_path))

    print_ok(f"WAR found: {war_path}")
    print_ok(f"Size: {size:.2f} MB")
    print_ok(f"Modified: {mod_time}")

    return mod_time

# ────────────────────────────────────────────────────────────────
def generate_sha256(war_path):
    print_section("STEP 2: SHA-256 Fingerprint")

    sha = hashlib.sha256()
    with open(war_path, "rb") as f:
        for chunk in iter(lambda: f.read(8192), b""):
            sha.update(chunk)

    hash_val = sha.hexdigest()
    print_ok(f"SHA-256: {hash_val}")
    print_info("Use this hash to match with artifact repo (Nexus/JFrog)")

    return hash_val

# ────────────────────────────────────────────────────────────────
def read_manifest(war_path):
    print_section("STEP 3: MANIFEST.MF")

    data = {}

    try:
        with zipfile.ZipFile(war_path, 'r') as war:
            if "META-INF/MANIFEST.MF" not in war.namelist():
                print_fail("MANIFEST.MF missing")
                return data

            with war.open("META-INF/MANIFEST.MF") as mf:
                content = mf.read().decode("utf-8", errors="ignore")

            for line in content.splitlines():
                if ":" in line:
                    k, _, v = line.partition(":")
                    data[k.strip()] = v.strip()

        for k, v in data.items():
            if k in ["Implementation-Build","Build-Time","Build-Branch","App-Version"]:
                print_ok(f"{k}: {v}")

        if "Implementation-Build" not in data:
            print_fail("Commit SHA not found in MANIFEST")

    except:
        print_fail("Invalid WAR file")

    return data

# ────────────────────────────────────────────────────────────────
def verify_git_commit(commit_sha):
    print_section("STEP 4: Git Verification")

    if not commit_sha:
        print_fail("No commit SHA available")
        return None

    try:
        result = subprocess.run(
            ["git","show","--format=%H|%an|%ai|%s|%D",commit_sha],
            capture_output=True,text=True
        )

        if result.returncode != 0:
            print_fail("Commit not found in repo")
            return None

        parts = result.stdout.split("|")

        info = {
            "sha": parts[0],
            "author": parts[1],
            "date": parts[2],
            "msg": parts[3],
            "branch": parts[4] if len(parts) > 4 else "unknown"
        }

        print_ok(f"Commit: {info['sha']}")
        print_ok(f"Author: {info['author']}")
        print_ok(f"Date: {info['date']}")
        print_ok(f"Message: {info['msg']}")
        print_ok(f"Branch: {info['branch']}")

        return info

    except:
        print_fail("Git not available")

    return None

# ────────────────────────────────────────────────────────────────
def fallback_using_time(war_mod_time):
    print_section("FALLBACK: Timestamp Matching")

    start = war_mod_time - timedelta(minutes=30)
    end   = war_mod_time + timedelta(minutes=30)

    try:
        result = subprocess.run(
            ["git","log","--since",str(start),"--until",str(end),"--oneline"],
            capture_output=True,text=True
        )

        if result.stdout.strip():
            print_ok("Possible commits near build time:")
            print(result.stdout)
        else:
            print_fail("No commits found in time range")

    except Exception as e:
        print_fail(f"Fallback error: {e}")

# ────────────────────────────────────────────────────────────────
def print_report(war, hash_val, manifest, commit_info, time):
    print(f"\n{BOLD}{'='*60}{RESET}")
    print(f"{BOLD} TRACEABILITY REPORT{RESET}")
    print(f"{BOLD}{'='*60}{RESET}")

    print(f"WAR: {war}")
    print(f"HASH: {hash_val[:20]}...")
    print(f"TIME: {time}")

    if not manifest.get("Implementation-Build"):
        print_info("Fallback used (commit not embedded)")

    if commit_info:
        print_ok("TRACEABILITY SUCCESS")
    else:
        print_fail("TRACEABILITY FAILED")

# ────────────────────────────────────────────────────────────────
def main():
    print_header()

    if len(sys.argv) > 1:
        war = sys.argv[1]
    else:
        war = "target/calculator-app.war"

    war_time = check_war_file(war)
    hash_val = generate_sha256(war)
    manifest = read_manifest(war)

    commit_sha = manifest.get("Implementation-Build")

    if not commit_sha:
        fallback_using_time(war_time)

    commit_info = verify_git_commit(commit_sha)

    print_report(war, hash_val, manifest, commit_info, war_time)

if __name__ == "__main__":
    main()
