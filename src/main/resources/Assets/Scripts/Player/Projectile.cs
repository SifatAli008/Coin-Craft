using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Projectile : MonoBehaviour
{
    [SerializeField] private float moveSpeed = 22f;
    [SerializeField] private GameObject hitEffect;
    [SerializeField] private bool isEnemyProjectile = false;
    [SerializeField] private float projectileRange = 10f;

    private Vector3 startPosition;

    private void Start()
    {
        startPosition = transform.position;
    }

    private void Update()
    {
        MoveProjectile();
        DetectFireDistance();
    }

    // Update the projectile range
    public void UpdateProjectileRange(float newRange)
    {
        projectileRange = newRange;
    }

    // Update the move speed of the projectile
    public void UpdateMoveSpeed(float newSpeed)
    {
        moveSpeed = newSpeed;
    }

    private void OnTriggerEnter2D(Collider2D other)
    {
        EnemyHealth enemyHealth = other.gameObject.GetComponent<EnemyHealth>();
        Indestructible indestructible = other.gameObject.GetComponent<Indestructible>();
        PlayerHealth player = other.gameObject.GetComponent<PlayerHealth>();

        if (!other.isTrigger && (enemyHealth || indestructible || player))
        {
            if ((player && isEnemyProjectile) || (enemyHealth && !isEnemyProjectile))
            {
                player?.TakeDamage(1, transform);
                InstantiateHitEffect();
                Destroy(gameObject);
            }
            else if (!other.isTrigger && indestructible)
            {
                InstantiateHitEffect();
                Destroy(gameObject);
            }
        }
    }

    // Check if the projectile has traveled beyond its range
    private void DetectFireDistance()
    {
        if (Vector3.Distance(transform.position, startPosition) > projectileRange)
        {
            Destroy(gameObject);
        }
    }

    // Move the projectile forward
    private void MoveProjectile()
    {
        transform.Translate(Vector3.right * Time.deltaTime * moveSpeed);
    }

    // Instantiate the hit effect if it's assigned
    private void InstantiateHitEffect()
    {
        if (hitEffect != null)
        {
            Instantiate(hitEffect, transform.position, transform.rotation);
        }
        else
        {
            Debug.LogWarning("Hit effect is not assigned in the inspector!");
        }
    }
}
