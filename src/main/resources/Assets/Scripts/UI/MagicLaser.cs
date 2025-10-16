using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MagicLaser : MonoBehaviour
{
    [SerializeField] private float laserGrowTime = 0.3f;
    [SerializeField] private int laserDamage = 1;

    private bool isGrowing = true;
    private float laserRange;
    private SpriteRenderer spriteRenderer;
    private CapsuleCollider2D capsuleCollider2D;
    private HashSet<EnemyHealth> enemiesHit = new HashSet<EnemyHealth>();

    private void Awake()
    {
        spriteRenderer = GetComponent<SpriteRenderer>();
        capsuleCollider2D = GetComponent<CapsuleCollider2D>();
    }

    private void Start()
    {
        LaserFaceMouse();
    }

    private void OnTriggerEnter2D(Collider2D other)
    {
        if (!other.isTrigger)
        {
            // Damage enemy if not already hit
            EnemyHealth enemyHealth = other.GetComponent<EnemyHealth>();
            if (enemyHealth != null && !enemiesHit.Contains(enemyHealth))
            {
                enemyHealth.TakeDamage(laserDamage);
                enemiesHit.Add(enemyHealth);
            }

            // Stop laser growth if it hits something indestructible
            if (other.GetComponent<Indestructible>() != null)
            {
                isGrowing = false;
            }
        }
    }

    public void UpdateLaserRange(float laserRange)
    {
        this.laserRange = laserRange;
        StartCoroutine(IncreaseLaserLengthRoutine());
    }

    private IEnumerator IncreaseLaserLengthRoutine()
    {
        float timePassed = 0f;

        while (spriteRenderer.size.x < laserRange && isGrowing)
        {
            timePassed += Time.deltaTime;
            float linearT = timePassed / laserGrowTime;
            float currentLength = Mathf.Lerp(0.1f, laserRange, linearT);

            spriteRenderer.size = new Vector2(currentLength, 1f);
            capsuleCollider2D.size = new Vector2(currentLength, capsuleCollider2D.size.y);
            capsuleCollider2D.offset = new Vector2(currentLength / 2f, capsuleCollider2D.offset.y);

            yield return null;
        }

        // Optional: Fade and destroy
        SpriteFade fade = GetComponent<SpriteFade>();
        if (fade != null)
        {
            StartCoroutine(fade.SlowFadeRoutine());
        }
        else
        {
            Destroy(gameObject, 0.5f);
        }
    }

    private void LaserFaceMouse()
    {
        Vector3 mousePosition = Input.mousePosition;
        mousePosition = Camera.main.ScreenToWorldPoint(mousePosition);
        Vector2 direction = transform.position - mousePosition;
        transform.right = -direction;
    }
}
