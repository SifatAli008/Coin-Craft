using UnityEngine;

public class Staff : MonoBehaviour, IWeapon
{
    [SerializeField] private WeaponInfo weaponInfo;
    [SerializeField] private GameObject magicLaser;
    [SerializeField] private Transform magicLaserSpawnPoint;

    private Animator myAnimator;

    private readonly int ATTACK_HASH = Animator.StringToHash("Attack");
    private float attackCooldown = 0.5f;
    private float lastAttackTime;

    private void Awake()
    {
        myAnimator = GetComponent<Animator>();
    }

    private void Update()
    {
        MouseFollowWithOffset();

        // Fire when left mouse is clicked and cooldown passed
        if (Input.GetMouseButtonDown(0) && Time.time > lastAttackTime + attackCooldown)
        {
            Attack();
            lastAttackTime = Time.time;
        }
    }

    public void Attack()
    {
        myAnimator.SetTrigger(ATTACK_HASH);
        FireLaser(); // Fire right after animation trigger
    }

    private void FireLaser()
    {
        if (magicLaser == null || magicLaserSpawnPoint == null)
        {
            Debug.LogError("MagicLaser or SpawnPoint not assigned in Inspector!");
            return;
        }

        GameObject newLaser = Instantiate(magicLaser, magicLaserSpawnPoint.position, Quaternion.identity);
        MagicLaser laserScript = newLaser.GetComponent<MagicLaser>();

        if (laserScript != null)
        {
            laserScript.UpdateLaserRange(weaponInfo.weaponRange);
            Debug.Log("Laser fired.");
        }
        else
        {
            Debug.LogError("MagicLaser prefab does not have MagicLaser.cs attached!");
        }
    }

    public WeaponInfo GetWeaponInfo()
    {
        return weaponInfo;
    }

    private void MouseFollowWithOffset()
    {
        Vector3 mousePos = Input.mousePosition;
        Vector3 playerScreenPoint = Camera.main.WorldToScreenPoint(PlayerController.Instance.transform.position);

        float angle = Mathf.Atan2(mousePos.y - playerScreenPoint.y, mousePos.x - playerScreenPoint.x) * Mathf.Rad2Deg;

        if (mousePos.x < playerScreenPoint.x)
        {
            ActiveWeapon.Instance.transform.rotation = Quaternion.Euler(0, -180, -angle);
        }
        else
        {
            ActiveWeapon.Instance.transform.rotation = Quaternion.Euler(0, 0, angle);
        }
    }
}
